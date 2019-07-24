package org.poem.heart;

import org.poem.SpringUtils;
import org.poem.event.ClientPushEvent;
import org.poem.event.ServiceEvent;
import org.poem.heart.client.HeartbeatClient;
import org.poem.listener.SystemClientApplicationService;
import org.poem.listener.SystemServiceApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author poem
 */
@Service
public class HeartExecutor implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(HeartExecutor.class);
    /**
     * 上下文对象
     */
    @Resource
    private ApplicationContext applicationContext;

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 手动提交
     * @param heartbeatClient
     */
    public static void  submit(HeartbeatClient heartbeatClient){
        executor.submit(heartbeatClient);
    }

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
    public void destroy(){
        logger.info("heartExecutor destroy ...");
        executor.shutdown();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent){
            logger.info( "start heartbeat ......." );
            run();
        }
    }
}
