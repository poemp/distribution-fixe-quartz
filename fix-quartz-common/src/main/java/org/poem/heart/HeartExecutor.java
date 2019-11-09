package org.poem.heart;

import org.poem.SpringUtils;
import org.poem.event.ClientPushEvent;
import org.poem.event.ServiceEvent;
import org.poem.heart.client.HeartbeatClient;
import org.poem.listener.SystemClientApplicationService;
import org.poem.listener.SystemServiceApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.poem.AwaitTermination.MAX_AWAIT;

/**
 * @author poem
 */
@Service
public class HeartExecutor implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(HeartExecutor.class);

    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    /**
     * 上下文对象
     */
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 手动提交
     *
     * @param heartbeatClient
     */
    public static void submit(HeartbeatClient heartbeatClient) {
        try {
            Thread.sleep(Heartbeat.TIME);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        if (!executor.isShutdown()) {
            executor.submit(heartbeatClient);
        }
    }

    /**
     * 执行
     */
    public void run() {
        submit(new HeartbeatClient(new HeartbeatHandler() {

            @Override
            public void handler() {

                SystemClientApplicationService systemClientApplicationService = SpringUtils.getBean(SystemClientApplicationService.class);
                if (systemClientApplicationService != null) {
                    ClientPushEvent event = systemClientApplicationService.client();
                    applicationContext.publishEvent(event);
                }

                SystemServiceApplicationService serviceApplicationService = SpringUtils.getBean(SystemServiceApplicationService.class);
                if (serviceApplicationService != null) {
                    ServiceEvent event = serviceApplicationService.service();
                    applicationContext.publishEvent(event);
                }
            }
        }));
    }


    @PreDestroy
    public void destroy() {
        logger.info("heartExecutor destroy ...");
        executor.shutdown();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent) {
            logger.info("start heartbeat .......");
            run();
        }
        //注册钩子，在应用程序结束之后，需要去执行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (logger.isDebugEnabled()) {
                logger.info("Executor Will Destroy .......");
            }
            executor.shutdown(); // 使新任务无法提交.
            try {
                // 等待未完成任务结束
                if (!executor.awaitTermination(MAX_AWAIT, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // 取消当前执行的任务
                    logger.warn("Interrupt the worker, which may cause some task inconsistent. Please check the biz logs.");

                    // 等待任务取消的响应
                    if (!executor.awaitTermination(MAX_AWAIT, TimeUnit.SECONDS)) {
                        logger.error("Thread pool can't be shutdown even with interrupting worker threads, which may cause some task inconsistent. Please check the biz logs.");
                    }
                }
            } catch (InterruptedException ie) {
                // 重新取消当前线程进行中断
                executor.shutdownNow();
                logger.error("The current server thread is interrupted when it is trying to stop the worker threads. This may leave an inconcistent state. Please check the biz logs.");

                // 保留中断状态
                Thread.currentThread().interrupt();
            }

            logger.info("Finally shutdown the thead pool .......");

        }));
    }
}
