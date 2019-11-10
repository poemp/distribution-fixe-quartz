package org.poem.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ApiModel("执行的操纵信息")
public class TransferInfo implements Serializable {

    /**
     * 调用的参数
     */
    @ApiModelProperty("参数")
    private List<TransferParametersInfo> transferParametersInfos;
    /**
     * 调用的类
     */
    @ApiModelProperty("调用的类")
    private String className;
    /**
     * 调用方法
     */
    @ApiModelProperty("调用方法")
    private String methodName;
    /**
     * appname
     */
    @ApiModelProperty("appname")
    private String appName;

}
