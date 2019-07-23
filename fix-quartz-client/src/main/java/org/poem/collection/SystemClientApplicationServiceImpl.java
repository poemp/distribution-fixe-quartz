package org.poem.collection;

import org.poem.collection.push.QuartzPulishHelper;
import org.poem.event.ClientPushEvent;
import org.poem.listener.SystemClientApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author poem
 */
@Service
public class SystemClientApplicationServiceImpl implements SystemClientApplicationService {

    @Autowired
    private QuartzPulishHelper quartzPulishHelper;

    @Override
    public ClientPushEvent client() {
        return new ClientPushEvent(new Object()) {

            @Override
            public void run() {
                quartzPulishHelper.push(QuartzCollections.getQuartzServiceClasses());
            }
        };
    }
}
