package top.gaogle.framework.security.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.exception.RateLimiterException;
import top.gaogle.framework.commons.util.IpUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.redis.service.StringRedisService;
import top.gaogle.framework.security.annotation.RateLimiter;
import top.gaogle.framework.security.enums.LimitTypeEnum;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流切面处理
 *
 * @author gaogle
 * @since 1.0.0
 */
@Aspect
@Component
public class RateLimiterAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);


    private final StringRedisService stringRedisService;

    @Autowired
    public RateLimiterAspect(StringRedisService stringRedisService) {
        this.stringRedisService = stringRedisService;
    }

    /**
     * 在方法执行前进行限流校验
     *
     * @param point       JoinPoint
     * @param rateLimiter RateLimiter 注解
     */
    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        String combineKey = buildCombineKey(rateLimiter, point);
        List<String> keys = Collections.singletonList(combineKey);
        String rateLimiterTime = String.valueOf(rateLimiter.time());
        Long requestCount = stringRedisService.executeLimitScript(keys, String.valueOf(rateLimiter.count()), rateLimiterTime);
        validateRequest(requestCount, rateLimiter.count(), combineKey, rateLimiterTime);
    }

    /**
     * 验证请求是否超过限流阈值
     *
     * @param requestCount 当前请求数
     * @param maxCount     最大允许请求数
     * @param combineKey   缓存键
     */
    private void validateRequest(Long requestCount, int maxCount, String combineKey, String rateLimiterTime) {
        if (StringUtil.isNull(requestCount) || requestCount > maxCount) {
            log.info("限制请求数 '{}', 当前请求数 '{}', 缓存 key '{}'", maxCount, requestCount, combineKey);
            throw new RateLimiterException(String.format("您访问过于频繁，请%s秒后再试！", rateLimiterTime));
        }
    }

    /**
     * 构建 Redis 缓存键
     *
     * @param rateLimiter RateLimiter 注解
     * @param point       JoinPoint
     * @return 缓存键
     */
    private String buildCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuilder keyBuilder = new StringBuilder(rateLimiter.key());

        if (rateLimiter.limitType() == LimitTypeEnum.IP) {
            keyBuilder.append(IpUtil.getIpAddr()).append("-");
        }

        Method method = ((MethodSignature) point.getSignature()).getMethod();
        keyBuilder.append(method.getDeclaringClass().getName())
                .append("-")
                .append(method.getName());

        return keyBuilder.toString();
    }
}
