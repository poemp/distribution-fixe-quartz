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

/**
 * @author Administrator
 */
public class InstanceInfoRepository {

    private static final Logger logger = LoggerFactory.getLogger( InstanceInfoRepository.class );
    /**
     * 失效次数
     */
    private static final AtomicInteger LOST_COUNT = new AtomicInteger( 10 );
    /**
     * 失效
     */
    private static final AtomicBoolean LOST_BOOLEAN = new AtomicBoolean( false );
    /**
     * 所有的数据
     */
    private static volatile ConcurrentLinkedQueue<Repository> list = new ConcurrentLinkedQueue<>();
    /**
     * 所有的id
     */
    private static volatile ConcurrentMap<String, Object> sets = new ConcurrentHashMap<String, Object>();


    private static ExecutorService executor = Executors.newFixedThreadPool( 2 );

    static {
        logger.info( "Start Monitor  ...... " );
        executor.submit( new MonitorFlush() );
        executor.submit( new MonitorRemove() );
    }

    /**
     * 添加
     *
     * @param instanceInfo
     */
    public static void add(QuartzInstanceInfo instanceInfo) {
        Object o = sets.get( instanceInfo.getId() );
        if (o == null) {
            Repository repository = new Repository();
            repository.setLoseCount( LOST_COUNT );
            repository.setLose( LOST_BOOLEAN );
            repository.setQuartzInstanceInfo( instanceInfo );
            list.add( repository );
            sets.put( instanceInfo.getId(), new Object() );
        } else {
            for (Repository r : list) {
                if (r.getQuartzInstanceInfo().getId().equals( instanceInfo.getId() )) {
                    r.setLoseCount( LOST_COUNT );
                    r.setLose( LOST_BOOLEAN );
                    r.setLose( new AtomicBoolean( false ) );
                    //还是把原来的放进去，如果新添加方法，不会更新原来保存的数据
                    r.setQuartzInstanceInfo( instanceInfo );
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
            if (r.getQuartzInstanceInfo().getId().equals( repository.getId() )) {
                r.setLose( new AtomicBoolean( true ) );
                delete = r;
                // 删除
                break;
            }
        }
        if (delete != null) {
            sets.remove( delete.getQuartzInstanceInfo().getId() );
            list.remove( delete );
        }
    }

    /**
     * @param repository
     */
    private static void remove(Repository repository) {
        Repository delete = null;
        for (Repository r : list) {
            if (r.getQuartzInstanceInfo().getId().equals( repository.getQuartzInstanceInfo().getId() )) {
                r.setLose( new AtomicBoolean( true ) );
                delete = r;
                // 删除
                break;
            }
        }
        if (delete != null) {
            logger.info( "Remove: " + delete.getQuartzInstanceInfo().getId() );
            sets.remove( delete.getQuartzInstanceInfo().getId() );
            list.remove( delete );
        }
    }

    /**
     * 获取数据
     */
    public static List<QuartzInstanceInfo> getAllInstanceInfo() {
        List<QuartzInstanceInfo> instanceInfos = Lists.newArrayList();
        for (Repository repository : list) {
            instanceInfos.add( repository.getQuartzInstanceInfo() );
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
                    repository.setLose( new AtomicBoolean( count.intValue() - 1 < 0 ) );
                    repository.setLoseCount( new AtomicInteger( count.intValue() - 1 < 0 ? 0 : count.intValue() - 1 ) );
                }
                Thread.sleep( Heartbeat.TIME );
                executor.submit( this );
            } catch (InterruptedException e) {
                logger.error( e.getMessage(),e );
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
                        remove( repository );
                    }
                }
                Thread.sleep( Heartbeat.TIME );
                executor.submit( this );
            } catch (InterruptedException e) {
                logger.error( e.getMessage(),e );
                e.printStackTrace();
            }
        }
    }
}
