package org.poem.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@ApiModel("执行参数的参数")
public class TransferParametersInfo implements Serializable {

    @ApiModelProperty("参数的类型")
    private String className;

    @ApiModelProperty("参数值")
    private Object value;
}
