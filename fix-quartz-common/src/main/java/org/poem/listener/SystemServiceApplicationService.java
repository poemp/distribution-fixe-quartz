package org.poem.listener;

import org.poem.event.ServiceEvent;

/**
 * 事件的发布
 *
 * @author poem
 */
public interface SystemServiceApplicationService {


    /**
     * 发布服务端事件
     */
    public ServiceEvent service();
}
