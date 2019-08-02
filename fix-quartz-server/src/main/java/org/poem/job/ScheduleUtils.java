package org.poem.job;

import com.google.gson.Gson;
import org.poem.exception.RRException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月30日 下午12:44:59
 */
public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_";

    private static final Logger logger = LoggerFactory.getLogger( ScheduleUtils.class );

    /**
     * 获取触发器key
     */
    private static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey( JOB_NAME + jobId );
    }

    /**
     * 获取触发器key
     */
    private static TriggerKey getTriggerKey(String jobId) {
        return TriggerKey.triggerKey( JOB_NAME + jobId );
    }

    /**
     * 获取jobKey
     */
    private static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey( JOB_NAME + jobId );
    }

    /**
     * 获取jobKey
     */
    private static JobKey getJobKey(String jobId) {
        return JobKey.jobKey( JOB_NAME + jobId );
    }

    /**
     * 获取表达式触发器
     */
    private static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger( getTriggerKey( jobId ) );
        } catch (SchedulerException e) {
            throw new RRException( "获取定时任务CronTrigger出现异常", e );
        }
    }

    /**
     * 获取表达式触发器
     */
    private static CronTrigger getCronTrigger(Scheduler scheduler, String jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger( getTriggerKey( jobId ) );
        } catch (SchedulerException e) {
            throw new RRException( "获取定时任务CronTrigger出现异常", e );
        }
    }


    /**
     * 自动创建
     *
     * @param scheduler
     * @param cronExpression
     * @throws SchedulerException
     */
    private static void createAuto(Scheduler scheduler, String cronExpression) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob( ScheduleJob.class ).withIdentity( getJobKey( "id" ) )
                .usingJobData( "key", "data" ).build();

        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule( cronExpression )
                .withMisfireHandlingInstructionDoNothing();

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity( getTriggerKey( "id" ) )
                .withSchedule( scheduleBuilder ).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put( "key", "data" );

        JobKey jobKey = getJobKey( "id" );
        if (scheduler.checkExists( jobKey )) {
            scheduler.deleteJob( jobKey );
        }
        scheduler.scheduleJob( jobDetail, trigger );

    }


    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob( getJobKey( jobId ) );
        } catch (SchedulerException e) {
            throw new RRException( "暂停定时任务失败", e );
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.pauseJob( getJobKey( jobId ) );
        } catch (SchedulerException e) {
            throw new RRException( "暂停定时任务失败", e );
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob( getJobKey( jobId ) );
        } catch (SchedulerException e) {
            throw new RRException( "暂停定时任务失败", e );
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob( getJobKey( jobId ) );
        } catch (SchedulerException e) {
            throw new RRException( "删除定时任务失败", e );
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.deleteJob( getJobKey( jobId ) );
        } catch (SchedulerException e) {
            throw new RRException( "删除定时任务失败", e );
        }
    }
}
