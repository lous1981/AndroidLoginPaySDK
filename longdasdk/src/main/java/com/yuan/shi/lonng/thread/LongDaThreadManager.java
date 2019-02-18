package com.yuan.shi.lonng.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/11
 */
public class LongDaThreadManager {
    private static final String TAG = LongDaThreadManager.class.getSimpleName();
    private static ThreadPoolProxy poolProxy;

    public static ThreadPoolProxy getPoolProxy() {
        if (poolProxy == null) {
            synchronized (TAG) {
                if (poolProxy == null) {
                    int processorCount = Runtime.getRuntime().availableProcessors();
                    int maxAvailable = Math.max(processorCount * 3, 10);

                    // 线程池的核心线程数、最大线程数，以及keepAliveTime都需要根据项目需要做修改
                    // PS：创建线程的开销 高于 维护线程(wait)的开销
                    poolProxy = new ThreadPoolProxy(processorCount, maxAvailable, 15000);
                }
            }
        }

        return poolProxy;
    }

    public static class ThreadPoolProxy {
        private ThreadPoolExecutor threadPoolExecutor;
        // 线程池
        private int corePoolSize;
        //线程池中核心线程数
        private int maximumPoolSize;
        //线程池中最大线程数，若并发数高于该数，后面的任务则会等待
        private int keepAliveTime;
        // 超出核心线程数的线程在执行完后保持alive时长

        /**
         * @param keepAliveTime time in milliseconds
         */
        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, int keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if (runnable == null) {
                return;
            } else {
                if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                    synchronized (TAG) {
                        if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                            threadPoolExecutor = createExecutor();
                            threadPoolExecutor.allowCoreThreadTimeOut(false);
                            // 核心线程始终不消失
                        }
                    }
                }

                threadPoolExecutor.execute(runnable);
            }
        }

        private ThreadPoolExecutor createExecutor() {
            return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(), new LongDaDefaultThreadFactory(Thread.NORM_PRIORITY, "my-pool-"),
                    new ThreadPoolExecutor.AbortPolicy());
        }
    }
}
