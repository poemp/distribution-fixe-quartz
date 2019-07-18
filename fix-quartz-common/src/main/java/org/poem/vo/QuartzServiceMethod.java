package org.poem.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author poem
 */
@Data
@NoArgsConstructor
public class QuartzServiceMethod {

    /**
     * 注释名字
     */
    private String methodName;

    /**
     * 注释的方法名字
     */
    private String method;


    /**
     * 参数列表
     */
    private List<QuartzServiceMethodParms> quartzServiceMethodParmsList;
}
