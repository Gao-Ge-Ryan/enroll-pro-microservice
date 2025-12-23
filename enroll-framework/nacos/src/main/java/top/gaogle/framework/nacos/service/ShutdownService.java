package top.gaogle.framework.nacos.service;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import org.springframework.stereotype.Service;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.commons.util.SpringUtil;
import top.gaogle.framework.nacos.util.WatchFunctionHelper;


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
            SpringUtil.getBean(NacosRegistration.class);
            log.info("将系统标记为不可用.");
            WatchFunctionHelper.markingSystemShutdown();

        } catch (Exception e) {
            log.error("shutdown error", e);
            result.failed().setMessage(e.getMessage());
        }
        return result;
    }
}
