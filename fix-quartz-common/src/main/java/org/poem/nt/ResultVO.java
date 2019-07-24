package org.poem.nt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author poem
 */
@ApiModel(description = "服务结果")
@Data
public class ResultVO<T> {

    @ApiModelProperty(value = "状态码.code为0表示成功,code为1表示失败")
    private int code = 0;
    @ApiModelProperty(value = "提示信息")
    private String info;
    @ApiModelProperty(value = "有效数据")
    private T data;

    public ResultVO() {
    }

    public ResultVO(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public ResultVO(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResultVO(int code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }


    public ResultVO(int code, T data, String info) {
        this.code = code;
        this.info = info;
        this.data = data;
    }
}
