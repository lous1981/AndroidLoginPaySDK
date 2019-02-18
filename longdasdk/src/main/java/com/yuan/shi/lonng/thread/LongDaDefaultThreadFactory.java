package com.yuan.shi.lonng.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/11
 */
public class LongDaDefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber   = new AtomicInteger(1); // 线程池的计数
    private final AtomicInteger threadNumber        = new AtomicInteger(1); // 线程的计数
    private final ThreadGroup group;
    private final String namePrefix;
    private final int  threadPriority;

    LongDaDefaultThreadFactory(int threadPriority, String threadNamePrefix) {
        this.threadPriority = threadPriority;
        this.group = Thread.currentThread().getThreadGroup();
        namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }

        t.setPriority(threadPriority);

        return t;
    }
}
