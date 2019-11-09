package org.poem.repository;

import com.google.common.collect.Lists;
import org.poem.QuartzInstanceInfo;
import org.poem.heart.Heartbeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.poem.AwaitTermination.MAX_AWAIT;

/**
 * @author Administrator
 */
public class InstanceInfoRepository {

    private static final Logger logger = LoggerFactory.getLogger(InstanceInfoRepository.class);
    /**
     * 失效次数
     */
    private static final AtomicInteger LOST_COUNT = new AtomicInteger(10);
    /**
     * 失效
     */
    private static final AtomicBoolean LOST_BOOLEAN = new AtomicBoolean(false);
    /**
     * 所有的数据
     */
    private static volatile ConcurrentLinkedQueue<Repository> list = new ConcurrentLinkedQueue<>();
    /**
     * 所有的id
     */
    private static volatile ConcurrentMap<String, Object> sets = new ConcurrentHashMap<String, Object>();


    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    static {
        logger.info("Start Monitor  ...... ");
        executor.submit(new MonitorFlush());
        executor.submit(new MonitorRemove());
        //注册钩子，在应用程序结束之后，需要去执行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (logger.isDebugEnabled()) {
                logger.debug("Executor Will Destroy .......");
            }
            executor.shutdown(); // 使新任务无法提交.
            try {
                // 等待未完成任务结束
                if (!executor.awaitTermination(MAX_AWAIT, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // 取消当前执行的任务
                    logger.warn("Interrupt the worker, which may cause some task inconsistent. Please check the biz logs.");

                    // 等待任务取消的响应
                    if (!executor.awaitTermination(MAX_AWAIT, TimeUnit.SECONDS)) {
                        logger.error("Thread pool can't be shutdown even with interrupting worker threads, which may cause some task inconsistent. Please check the biz logs.");
                    }
                }
            } catch (InterruptedException ie) {
                // 重新取消当前线程进行中断
                executor.shutdownNow();
                logger.error("The current server thread is interrupted when it is trying to stop the worker threads. This may leave an inconcistent state. Please check the biz logs.");

                // 保留中断状态
                Thread.currentThread().interrupt();
            }

            logger.info("Finally shutdown the thead pool .......");

        }));
    }


    /**
     * 添加
     *
     * @param instanceInfo
     */
    public static void add(QuartzInstanceInfo instanceInfo) {
        Object o = sets.get(instanceInfo.getId());
        if (o == null) {
            Repository repository = new Repository();
            repository.setLoseCount(LOST_COUNT);
            repository.setLose(LOST_BOOLEAN);
            repository.setQuartzInstanceInfo(instanceInfo);
            list.add(repository);
            sets.put(instanceInfo.getId(), new Object());
        } else {
            for (Repository r : list) {
                if (r.getQuartzInstanceInfo().getId().equals(instanceInfo.getId())) {
                    r.setLoseCount(LOST_COUNT);
                    r.setLose(LOST_BOOLEAN);
                    r.setLose(new AtomicBoolean(false));
                    //还是把原来的放进去，如果新添加方法，不会更新原来保存的数据
                    r.setQuartzInstanceInfo(instanceInfo);
                }
            }
        }
    }

    /**
     * @param repository
     */
    public static void remove(QuartzInstanceInfo repository) {
        Repository delete = null;
        for (Repository r : list) {
            if (r.getQuartzInstanceInfo().getId().equals(repository.getId())) {
                r.setLose(new AtomicBoolean(true));
                delete = r;
                // 删除
                break;
            }
        }
        if (delete != null) {
            sets.remove(delete.getQuartzInstanceInfo().getId());
            list.remove(delete);
        }
    }

    /**
     * @param repository
     */
    private static void remove(Repository repository) {
        Repository delete = null;
        for (Repository r : list) {
            if (r.getQuartzInstanceInfo().getId().equals(repository.getQuartzInstanceInfo().getId())) {
                r.setLose(new AtomicBoolean(true));
                delete = r;
                // 删除
                break;
            }
        }
        if (delete != null) {
            logger.info("Remove: " + delete.getQuartzInstanceInfo().getId());
            sets.remove(delete.getQuartzInstanceInfo().getId());
            list.remove(delete);
        }
    }

    /**
     * 获取数据
     */
    public static List<QuartzInstanceInfo> getAllInstanceInfo() {
        List<QuartzInstanceInfo> instanceInfos = Lists.newArrayList();
        for (Repository repository : list) {
            instanceInfos.add(repository.getQuartzInstanceInfo());
        }
        return instanceInfos;
    }

    /**
     * 自动
     * 执行监控 刷新所有列表的状态
     */
    static class MonitorFlush implements Runnable {

        /**
         * 异步执行
         */
        @Override
        public void run() {
            try {
                for (Repository repository : list) {
                    AtomicInteger count = repository.getLoseCount();
                    repository.setLose(new AtomicBoolean(count.intValue() - 1 < 0));
                    repository.setLoseCount(new AtomicInteger(Math.max(count.intValue() - 1, 0)));
                }
                Thread.sleep(Heartbeat.TIME);
                if (!executor.isShutdown()) {
                    executor.submit(this);
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }

        }
    }

    /**
     * 执行删除任务
     */
    static class MonitorRemove implements Runnable {
        @Override
        public void run() {
            try {
                for (Repository repository : list) {
                    if (repository.getLose().get()) {
                        remove(repository);
                    }
                }
                Thread.sleep(Heartbeat.TIME);
                if (!executor.isShutdown()) {
                    executor.submit(this);
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }
}
