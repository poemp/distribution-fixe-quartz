package org.poem.nt.field;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NtField {

    /**
     * 名字不能为空
     *
     * @return
     */
    String name();

    /**
     * 是否是日期
     * xxxx-xx-xx
     *
     * @return
     */
    boolean isDate() default false;


    /**
     * 是否是身份证
     *
     * @return
     */
    boolean isIdNum() default false;


    /**
     * 是否是数字
     *
     * @return
     */
    boolean isNumber() default false;

}
