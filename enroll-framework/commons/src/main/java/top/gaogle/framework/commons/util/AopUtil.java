package top.gaogle.framework.commons.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;

public class AopUtil extends AopUtils {

    /**
     * 获取joinPoint的方法返回值类型
     */
    public static Class<?> getMethodReturnType(JoinPoint joinPoint) {
        return (Class<?>) ((MethodSignature)joinPoint.getSignature()).getReturnType();
    }

    /**
     * 切点方法全名
     */
    public static String getMethodFullName(JoinPoint joinPoint) {
        return String.format("%s.%s",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }
}
