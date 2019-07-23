package org.poem.event;

import org.poem.event.interfaces.Event;
import org.springframework.context.ApplicationEvent;

/**
 * @author poem
 */
public abstract class BaseAbstractEvent  extends ApplicationEvent implements Event {

    BaseAbstractEvent(Object source) {
        super(source);
    }

    /**
     *
     */
    @Override
    public abstract void run();

}
