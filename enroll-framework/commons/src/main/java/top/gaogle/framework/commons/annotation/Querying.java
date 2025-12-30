

package top.gaogle.framework.commons.annotation;

import java.lang.annotation.*;

/**
 * 查询参数注解
 *
 * @author gaogle
 * @since 2.0.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Querying {

    Class<?> norm() default Querying.class;

    Naming sortFieldNaming() default Naming.SNAKE;

    enum Naming {
        SNAKE, STAY_AS
    }

}
