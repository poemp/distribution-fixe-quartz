package org.poem.nt.excel.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@Api(value = "倒入数据返回消息")
public class ExcelVO<T> {

    /**
     * 错误消息
     */
    @ApiModelProperty("错误消息")
    private List<String> messages;

    /**
     * 是否错误
     */
    @ApiModelProperty("是否错误")
    private Boolean err = false;

    /**
     * 成功后的数据
     */
    @ApiModelProperty("成功后的数据")
    private List<T> data;
    /**
     * 表头
     */
    @ApiModelProperty("表头")
    private List<String> head;

    /**
     * redis key
     */
    @ApiModelProperty("redis key")
    private String redisKey;

    public List<String> getHead() {
        return head;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }

    public Boolean getErr() {
        return err;
    }

    public void setErr(Boolean err) {
        this.err = err;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }
}
