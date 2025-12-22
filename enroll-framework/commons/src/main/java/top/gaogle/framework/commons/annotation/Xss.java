package top.gaogle.framework.commons.annotation;


import top.gaogle.framework.commons.validator.XssValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义xss校验注解
 *
 * @author gaogle
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = XssValidator.class)
public @interface Xss {
    String message() default "非法输入, 检测到潜在的XSS";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

