package top.gaogle.framework.security.aspect;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.util.AopUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.security.annotation.WatchFunction;
import top.gaogle.framework.security.util.WatchFunctionHelper;


/**
 * 监控功能切面
 *
 * @author Gaogle
 * @since 1.0.0
 */
@Aspect
@Component
public class WatchFunctionAop {
    private static final Logger logger = LoggerFactory.getLogger(WatchFunctionAop.class);

    @Around("@annotation(watchFunction)")
    public Object around(
            ProceedingJoinPoint joinPoint,
            WatchFunction watchFunction) throws Throwable {
        if (watchFunction.preCheckSysStatus() && !WatchFunctionHelper.ifSystemStatusEnabled()) {
            try {
                Class<?> returnTypeClazz = AopUtil.getMethodReturnType(joinPoint);
                logger.warn("system was disabled. this invoke is intercepted by AOP");
                return (returnTypeClazz.equals(Void.TYPE)) ? (Void) null : null;
            } catch (Throwable e) {
                logger.error(e.toString(), e);
            }
        }

        String workerName = null;
        try {
            String functionName = watchFunction.value();
            if (StringUtils.isBlank(functionName)) {
                functionName = AopUtil.getMethodFullName(joinPoint);
            }
            workerName = String.format("%s_%s", functionName, StringUtil.UUID());
            WatchFunctionHelper.add(workerName, functionName);
            //
            return joinPoint.proceed();
        } finally {
            if (workerName != null) {
                WatchFunctionHelper.remove(workerName);
            }
        }
    }
}
