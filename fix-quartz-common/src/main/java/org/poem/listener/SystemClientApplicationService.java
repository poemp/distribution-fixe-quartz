package org.poem.listener;

import org.poem.event.ClientPushEvent;

/**
 * 事件的发布
 * @author poem
 */
public interface SystemClientApplicationService {

    /**
     * 执行的方法
     * @return
     */
    public ClientPushEvent client();
}
