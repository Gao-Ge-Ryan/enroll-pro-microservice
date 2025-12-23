package top.gaogle.framework.security.annotation;

import java.lang.annotation.*;

/**
 * 检查点注解
 *
 * @author Gaogle
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WatchFunctionCheckPoint {
    String value() default ""; // 检查点名称
}
