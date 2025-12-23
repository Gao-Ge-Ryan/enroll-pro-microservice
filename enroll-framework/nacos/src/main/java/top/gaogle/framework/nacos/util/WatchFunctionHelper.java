package top.gaogle.framework.nacos.util;


import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gaogle.framework.commons.util.JsonUtil;
import top.gaogle.framework.commons.util.ReflectTypeUtil;
import top.gaogle.framework.commons.util.SpringUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.nacos.annotation.WatchFunctionCheckPoint;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 监控功能工具类
 *
 * @author Gaogle
 * @since 1.0.0
 */
public class WatchFunctionHelper {
    private static final Logger logger = LoggerFactory.getLogger(WatchFunctionHelper.class);

    private static final AtomicBoolean system_global_status_enabled = new AtomicBoolean(true);

    private static final ConcurrentHashMap<String, String> watched_service = new ConcurrentHashMap<>();

    /**
     * 从指定的包搜索检查点
     */
    public static String[] check_point_scan_pkg = null;

    /**
     * 检查点数据
     */
    private static Map<Method, Set<Object>> check_point_mapping = null;

    /**
     * 将需要被关注的方法加到队列. 一般是方法一开始的时候,将本方法加到队列.
     *
     * @param workerName   方法执行者
     * @param functionName 方法的全称或者唯一称呼.
     */
    public static void add(String workerName, String functionName) {
        watched_service.put(workerName, functionName);
    }

    /**
     * 将方法从关注队列中移除. 一般是方法结束的时候,将本方法从队列移除.
     *
     * @param workerName 方法执行者
     */
    public static void remove(String workerName) {
        watched_service.remove(workerName);
    }

    /**
     * 标记为系统不可用
     */
    public static void markingSystemShutdown() {
        system_global_status_enabled.set(false);
    }

    /**
     * 判断系统是否可用.
     *
     * @return 是否
     */
    public static boolean ifSystemStatusEnabled() {
        return system_global_status_enabled.get();
    }

    /**
     * 被关注的方法是否都已停止运行.
     *
     * @return 是否
     */
    public static boolean ifAllFunctionStopped() {
        return watched_service.isEmpty() && checkPointOneByOne() == null;
    }

    public static String getRunningInfo() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("运行中的方法", JsonUtil.object2JsonByJackson(watched_service));
        info.put("不通过的检查点", checkPointOneByOne());
        return JsonUtil.object2JsonByJackson(info);
    }

    /**
     * 获取当前被watch的service
     *
     * @return json格式的watch信息.
     */
    public static String getWatchedService() {
        return JsonUtil.object2JsonByJackson(watched_service);
    }

    /**
     * 遍历全部的检查点, 一旦遇到未正常返回的检查点, 随即中断检查并返回该检查结果,
     *
     * @return null: 所在系统未设置检查点或全部检查点都正常; 字符串描述: 未正常返回的检查点的检查自述
     */
    public static String checkPointOneByOne() {
        // 只有从未加载过, 才遍历加载.
        // 目前不支持动态增减检查点, 即整个运行期间检查点是不变的, 所以只需加载一次即可. 以此也能节省整体的检查时间.
        if (check_point_mapping == null) {
            scanCheckPoint();
        }
        if (check_point_mapping == null || check_point_mapping.isEmpty()) {
            logger.info("check_point_mapping is empty.");
            return null;
        }

        for (Map.Entry<Method, Set<Object>> methodAndInstances : check_point_mapping.entrySet()) {
            Method method = methodAndInstances.getKey();
            String fullName = method.toString();
            Set<Object> instances = methodAndInstances.getValue();
            for (Object instance : instances) {
                try {
                    Object result = method.invoke(instance);
                    String msg = StringUtil.replaceOneByOne("check point: {}, result: {}", fullName, result);
                    logger.info(msg);
                    if ((result instanceof Boolean && Boolean.FALSE.equals(result)) || result != null) {
                        return msg;
                    }
                } catch (Exception e) {
                    logger.error(e.toString(), e);
                }
            }
        }
        return null;
    }

    /**
     * 遍历加载检查点
     */
    public static void scanCheckPoint(String... pkg) {
        try {
            if (pkg == null || pkg.length == 0) {
                if (check_point_scan_pkg == null || check_point_scan_pkg.length == 0) {
                    // 若未传入参数值, 而且类属性值也没有值, 则无法确定包路径而退出.
                    return;
                }
            } else {
                // 若方法参数有值, 则覆盖类属性值.
                check_point_scan_pkg = pkg;
            }
            List<Method> methodList = ReflectTypeUtil.scanMethodWithAnnotation(WatchFunctionCheckPoint.class, check_point_scan_pkg);
            Map<Method, Set<Object>> mapping = new HashMap<>();
            methodList.forEach(method -> {
                logger.info("check point: {}", method.toString());
                Collection<?> instances = SpringUtil.getBeans(method.getDeclaringClass());
                if (CollectionUtils.isEmpty(instances)) {
                    return;
                }
                mapping.put(method, new HashSet<>(instances));
            });
            check_point_mapping = mapping;
        } finally {
            // 确保只要本方法执行过,存放检查点的map就一定不为null
            if (check_point_mapping == null) {
                check_point_mapping = new HashMap<>();
            }
        }

    }
}
