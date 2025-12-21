package top.gaogle.framework.security.annotation;


import top.gaogle.framework.commons.common.CommonsConst;
import top.gaogle.framework.security.enums.LimitTypeEnum;

import java.lang.annotation.*;

/**
 * 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    public String key() default CommonsConst.RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒
     */
    public int time() default 60;

    /**
     * 限流次数
     */
    public int count() default 100;

    /**
     * 限流类型
     */
    public LimitTypeEnum limitType() default LimitTypeEnum.DEFAULT;
}
