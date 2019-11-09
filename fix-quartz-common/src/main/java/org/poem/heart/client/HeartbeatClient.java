package org.poem.heart.client;

import lombok.Data;
import org.poem.heart.HeartExecutor;
import org.poem.heart.HeartbeatHandler;

/**
 * @author poem
 */
@Data
public class HeartbeatClient implements Runnable {


    private final HeartbeatHandler heartbeatHandler;

    private boolean isRunning = true;

    public HeartbeatClient(HeartbeatHandler heartbeatHandler) {
        this.heartbeatHandler = heartbeatHandler;
    }

    @Override
    public void run() {
        try {
            this.heartbeatHandler.handler();
            HeartExecutor.submit(this);
        } catch (Exception e) {
            e.printStackTrace();
            HeartExecutor.submit(this);
        }
    }
}

