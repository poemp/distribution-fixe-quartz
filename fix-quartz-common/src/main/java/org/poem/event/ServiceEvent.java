package org.poem.event;

/**
 * client 事件
 * 发送消息心跳事件
 */
public abstract class ServiceEvent extends BaseAbstractEvent{

    public ServiceEvent(Object source) {
        super(source);
    }

    /**
     *
     */
    @Override
    public abstract void run();

}
