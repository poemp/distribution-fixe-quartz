package org.poem.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 方法的参数
 * @author poem
 */
@Data
@NoArgsConstructor
public class QuartzServiceMethodParms {

    /**
     * 参数名称
     */
    private String name;

    /**
     * 方法的参数的类型
     */
    private Class clazz;

    /**
     * 描述
     */
    private String detail;


    public QuartzServiceMethodParms(String name, Class clazz, String detail) {
        this.name = name;
        this.clazz = clazz;
        this.detail = detail;
    }
}
