package org.poem.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * ClassName: ScheduleJob Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). date: 2019年4月16日 上午11:17:34
 *
 * @author Administrator
 * @since JDK 1.7
 */
public class ScheduleJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

    /**
     * @param context
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String jsonJob = context.getMergedJobDataMap().getString("key");
    }

}
