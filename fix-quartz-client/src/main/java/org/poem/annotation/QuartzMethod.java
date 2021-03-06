package org.poem.annotation;


import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QuartzMethod {

    /**
     * 方法的名字
     *
     * @return
     */
    String name() default "";
}
