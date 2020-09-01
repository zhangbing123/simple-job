package com.schedule.simplejob.timer;

import com.schedule.simplejob.exchandler.DefaultTaskExceptionHandler;
import com.schedule.simplejob.exchandler.TaskExceptionHandler;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-08-31 16:12
 **/
public class TimeRunTask implements Runnable {

    private SimpleJob simpleJob;

    private Runnable runnable;

    private long period;

    private boolean isDelay;

    private TaskExceptionHandler exceptionHandler;

    public TimeRunTask(SimpleJob simpleJob, Runnable runnable, long period, TaskExceptionHandler exceptionHandler) {
        this(simpleJob, runnable, period, true, exceptionHandler);
    }

    public TimeRunTask(SimpleJob simpleJob, Runnable runnable, long period, boolean isDelay, TaskExceptionHandler exceptionHandler) {
        this.simpleJob = simpleJob;
        this.runnable = runnable;
        this.period = period;
        this.isDelay = isDelay;
        this.exceptionHandler = exceptionHandler == null ? new DefaultTaskExceptionHandler() : exceptionHandler;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            runnable.run();
        } catch (Exception e) {
            //进入异常处理逻辑
            exceptionHandler.handle(e, this);
        }
        if (isDelay) {
            simpleJob.registerAtTime(System.currentTimeMillis() + period, this);
        } else {
            simpleJob.registerAtTime(startTime + period, this);
        }
    }


}
