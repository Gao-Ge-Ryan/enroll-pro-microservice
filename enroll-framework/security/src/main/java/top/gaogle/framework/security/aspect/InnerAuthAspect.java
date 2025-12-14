package top.gaogle.framework.security.aspect;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.common.SecurityConstants;
import top.gaogle.framework.commons.exception.auth.InnerAuthException;
import top.gaogle.framework.commons.util.ServletUtil;
import top.gaogle.framework.security.annotation.InnerAuth;

/**
 * 内部服务调用验证处理
 *
 * @author gaogle
 * @since 1.0.0
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered {

    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        String source = ServletUtil.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals(SecurityConstants.INNER, source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        String userid = ServletUtil.getRequest().getHeader(SecurityConstants.DETAILS_USER_ID);
        String username = ServletUtil.getRequest().getHeader(SecurityConstants.DETAILS_USERNAME);
        // 用户信息验证
        if (innerAuth.isUser() && (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username))) {
            throw new InnerAuthException("没有设置用户信息，不允许访问 ");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
