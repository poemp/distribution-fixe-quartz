package org.poem.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.poem.instanceinfo.QuartzServiceMethodParms;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@ApiModel("执行的操纵信息")
public class TransferInfo implements Serializable {

    /**
     * 调用的参数
     */
    @ApiModelProperty("参数")
    private List<QuartzServiceMethodParms> quartzServiceMethodParms;
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

    /**
     * port
     */
    @ApiModelProperty("端口")
    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<QuartzServiceMethodParms> getQuartzServiceMethodParms() {
        return quartzServiceMethodParms;
    }

    public void setQuartzServiceMethodParms(List<QuartzServiceMethodParms> quartzServiceMethodParms) {
        this.quartzServiceMethodParms = quartzServiceMethodParms;
    }
}
