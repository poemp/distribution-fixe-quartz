package org.poem.event;

/**
 * client 事件
 * 发送消息心跳事件
 */
public abstract class ClientPushEvent extends BaseAbstractEvent{

    public ClientPushEvent(Object source) {
        super(source);
    }

    /**
     *
     */
    @Override
    public abstract void run();

}
