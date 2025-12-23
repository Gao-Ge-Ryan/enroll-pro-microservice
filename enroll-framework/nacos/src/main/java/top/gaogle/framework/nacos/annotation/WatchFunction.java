package top.gaogle.framework.nacos.annotation;

import java.lang.annotation.*;

/**
 * 监控功能注解
 *
 * @author Gaogle
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WatchFunction {
    String value() default ""; // 功能名称

    boolean preCheckSysStatus() default false; // 是否先检查系统状态.
}
