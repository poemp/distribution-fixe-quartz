package org.poem.annotation;


import java.lang.annotation.*;

/**
 * @author poem
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QuartzService {

    /**
     * 执行的方法
     *
     * @return
     */
    String name() default "";
}
