package org.poem.repository;

import lombok.Data;
import org.poem.QuartzInstanceInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 */
@Data
public class Repository {

    /**
     * 保存的值
     */
    private QuartzInstanceInfo quartzInstanceInfo;

    /**
     * 淘汰制度
     * 失效的次数 3 次为疑似失效
     * 失效3次以上，则直接丢弃
     */
    private AtomicInteger loseCount;

    /**
     * 是否丢失
     */
    private AtomicBoolean lose;


    /**
     * 任务的状态
     */
    private List<RepositoryExecResult> repositoryExecResultList;
}
