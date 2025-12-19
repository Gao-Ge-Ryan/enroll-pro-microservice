package top.gaogle.framework.log.annotation;


import org.springframework.core.annotation.AliasFor;
import top.gaogle.framework.log.enums.BusinessTypeEnum;
import top.gaogle.framework.log.enums.OperatorTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author gaogle
 * @since 1.0.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    @AliasFor("value")
    String title() default "";

    /**
     * 功能
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    /**
     * 操作人类别
     */
    OperatorTypeEnum operatorType() default OperatorTypeEnum.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};

    @AliasFor("title")
    String value() default "";
}
