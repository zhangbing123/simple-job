package com.schedule.simplejob.timer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 任务执行器
 * @author: zhangbing
 * @create: 2020-09-02 10:46
 **/
public class TaskExecutor extends ThreadPoolExecutor {

    public TaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TaskExecutor() {
        this(10, 60, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
    }

    public void runTask(List<Runnable> taskAndRmv) {

        for (Runnable runnable : taskAndRmv) {
            execute(runnable);
        }
    }
}
