package org.poem.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.poem.JobStatus;
import org.poem.QuartzInstanceInfo;

import java.sql.Timestamp;

/**
 * 执行的结果
 *
 * @author Administrator
 */
@Data
@ApiModel("执行的结果")
public class RepositoryExecResult {

    /**
     * 任务结束时间
     */
    @ApiModelProperty("任务开始时间")
    private Timestamp startTimeStamp;

    /**
     * 任务结束时间
     */
    @ApiModelProperty("任务结束时间")
    private Timestamp endTimeStamp;

    /**
     * 执行的时长
     */
    @ApiModelProperty("执行的时长")
    private Long executorTime;

    /**
     * 任务状态
     */
    @ApiModelProperty("任务状态")
    private JobStatus status;


    /**
     * 请求的信息
     */
    @ApiModelProperty("请求的信息")
    private QuartzInstanceInfo quartzInstanceInfo;
}
