package org.poem.collection;

import org.poem.collection.push.QuartzPulishHelper;
import org.poem.event.ClientPushEvent;
import org.poem.listener.SystemClientApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

/**
 * @author poem
 */
@Service
public class SystemClientApplicationServiceImpl implements SystemClientApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(SystemClientApplicationServiceImpl.class);

    @Autowired
    private QuartzPulishHelper quartzPulishHelper;

    @Override
    public ClientPushEvent client() {
        return new ClientPushEvent(new Object()) {

            @Override
            public void run() {
                logger.info(" push ...... ");
                quartzPulishHelper.push(QuartzCollections.getQuartzServiceClasses());
            }
        };
    }

    @PreDestroy
    public void destroyClient() {
        logger.info(" Destroy Client ...... ");
        quartzPulishHelper.delete();
    }
}
