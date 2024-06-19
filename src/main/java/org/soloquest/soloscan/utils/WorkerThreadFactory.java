package org.soloquest.soloscan.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WorkerThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final boolean daemon;
    private final int priority;
    private final ThreadGroup threadGroup;

    public WorkerThreadFactory(String namePrefix) {
        this(namePrefix, true);
    }

    public WorkerThreadFactory(String namePrefix, boolean daemon) {
        this(namePrefix, daemon, 5);
    }

    public WorkerThreadFactory(String namePrefix, boolean daemon, int priority) {
        this(namePrefix, daemon, priority, null);
    }

    public WorkerThreadFactory(String namePrefix, boolean daemon, int priority, ThreadGroup threadGroup) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.priority = priority;
        this.threadGroup = threadGroup;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r, namePrefix + "-thread-" + threadNumber.getAndIncrement());
        t.setDaemon(daemon);
        t.setPriority(priority);
        t.setUncaughtExceptionHandler(this);
        return t;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("t:{} uncaughtException", e);
    }
}
