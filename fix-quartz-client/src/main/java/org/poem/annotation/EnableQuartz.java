package org.poem.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启
 *
 * @author poem
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableQuartz {

    /**
     * 扫描的包
     *
     * @return
     */
    String scan() default "";
}
