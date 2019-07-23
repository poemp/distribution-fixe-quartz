package org.poem.listener;

import com.sun.istack.internal.NotNull;
import org.poem.event.BaseAbstractEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 事件的监听和执行
 * @author poem
 */
@Component
public class SystemApplicationListener implements ApplicationListener<BaseAbstractEvent> {


    /**
     * 发布事件的执行
     * @param abstractEvent 事件
     */
    @NotNull
    @Override
    public void onApplicationEvent(BaseAbstractEvent abstractEvent) {
        abstractEvent.run();
    }
}

