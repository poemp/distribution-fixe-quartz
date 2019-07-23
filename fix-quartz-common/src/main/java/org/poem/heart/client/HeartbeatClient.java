package org.poem.heart.client;

import lombok.Data;
import org.poem.heart.HeartExecutor;
import org.poem.heart.HeartbeatHandler;

/**
 * @author poem
 */
@Data
public class HeartbeatClient implements Runnable {


    private boolean isRunning = true;
    /**
     * 最近的心跳时间
     */
    private long lastHeartbeat;
    /**
     * 心跳间隔时间
     */
    private long heartBeatInterval = 10 * 1000;

    private final HeartbeatHandler heartbeatHandler;


    public HeartbeatClient(HeartbeatHandler heartbeatHandler) {
        this.heartbeatHandler = heartbeatHandler;
    }

    @Override
    public void run() {

        while (isRunning) {
            long startTime = System.currentTimeMillis();
            // 是否达到发送心跳的周期时间
            if (startTime - lastHeartbeat > heartBeatInterval) {
                lastHeartbeat = startTime;
                try {
                    this.heartbeatHandler.handler();
                } catch (Exception e) {
                    e.printStackTrace();
                    HeartExecutor.submit(this);
                }
            }
        }
    }

}

