package org.poem.instanceinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 方法的参数
 *
 * @author poem
 */
@Data
@NoArgsConstructor
@ApiModel("操作参数")
public class QuartzServiceMethodParms {

    /**
     * 参数名称
     */
    @ApiModelProperty("参数名称")
    private String name;

    /**
     * 方法的参数的类型
     */
    @ApiModelProperty("方法的参数的类型")
    private Class clazz;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String detail;


    public QuartzServiceMethodParms(String name, Class clazz, String detail) {
        this.name = name;
        this.clazz = clazz;
        this.detail = detail;
    }
}
