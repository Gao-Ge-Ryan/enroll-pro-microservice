package top.gaogle.framework.nacos.service;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.commons.util.SpringUtil;
import top.gaogle.framework.nacos.handler.ShutdownHandler;
import top.gaogle.framework.nacos.util.WatchFunctionHelper;

import java.util.Collection;
import java.util.concurrent.TimeUnit;


/**
 * 停止服务
 *
 * @author Gaogle
 * @since 1.0.0
 */
@Service
public class ShutdownService extends SuperService {

    public I18nResult<Object> shutdown() {
        I18nResult<Object> result = I18nResult.newInstance();
        try {
            log.info("主动从微服务集群中断开注册");
            NacosServiceRegistry nacosServiceRegistry = SpringUtil.getBean(NacosServiceRegistry.class);
            NacosRegistration registration = SpringUtil.getBean(NacosRegistration.class);
            // 注销当前实例
            nacosServiceRegistry.deregister(registration);
            log.info("已从 Nacos 成功注销实例: {}:{} (service: {})",
                    registration.getHost(),
                    registration.getPort(),
                    registration.getServiceId());

            log.info("将系统标记为不可用");
            WatchFunctionHelper.markingSystemShutdown();

            log.info("调用ShutdownHandler");
            Collection<ShutdownHandler> shutdownHandlers = SpringUtil.getBeans(ShutdownHandler.class);
            if (CollectionUtils.isNotEmpty(shutdownHandlers)) {
                for (ShutdownHandler shutdownHandler : shutdownHandlers) {
                    I18nResult<Object> shutdownResult = shutdownHandler.shutdown();
                    if (shutdownResult.isFailed()) {
                        return shutdownResult;
                    }
                }
            }

            log.info("给各个功能点一定的收尾时间");
            final int maxAttempts = 5; // 对应 5 * 5s = 25s
            final int eachSleepSecond = 5;
            for (int i = 0; i < maxAttempts; i++) {
                if (WatchFunctionHelper.ifAllFunctionStopped()) {
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(eachSleepSecond);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    break; // 或抛出异常，根据业务决定
                }
            }
            if (!WatchFunctionHelper.ifAllFunctionStopped()) {
                log.info("仍旧存在正在执行的方法");
                return result.failed().setMessage("尚存在正在执行的方法").setData(WatchFunctionHelper.getRunningInfo());
            }
            result.succeed().setData(true);
        } catch (Exception e) {
            log.error("shutdown 失败：", e);
            result.failed().setMessage(e.getMessage());
        }
        return result;
    }
}
