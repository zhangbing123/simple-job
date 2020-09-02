package com.schedule.simplejob.timer;

import com.schedule.simplejob.exchandler.TaskExceptionHandler;
import com.schedule.simplejob.queue.TaskQueue;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-08-31 16:02
 **/
@Slf4j
public class SimpleJob {

    private TaskQueue queue;

    private TimeMonitor monitor;

    public SimpleJob() {

        this.queue = new TaskQueue();
        monitor = new TimeMonitor(queue);
    }


    /**
     * 在指定的时间执行
     *
     * @param time     执行的时间点
     * @param runnable 执行任务
     */
    public void registerAtTime(long time, Runnable runnable) {

        synchronized (queue) {

            queue.addQueue(time, runnable);

            queue.notify();
        }

        log.info("task registration successful");
    }


    /**
     * 间隔重复执行 任务执行结束开始计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerWithFixedDelay(long time, long period, Runnable runnable) {

        registerWithFixedDelay(time, period, runnable, null);
    }

    /**
     * 间隔重复执行 任务执行结束开始计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerWithFixedDelay(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler) {

        registerAtTime(System.currentTimeMillis() + time, new TimeRunTask(this, runnable, period, exceptionHandler));
    }


    /**
     * 间隔重复执行 任务执行开始时计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerAtFixedRate(long time, long period, Runnable runnable) {
        registerAtFixedRate(time, period, runnable, null);
    }

    /**
     * 间隔重复执行 任务执行开始时计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerAtFixedRate(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler) {
        registerAtTime(System.currentTimeMillis() + time, new TimeRunTask(this, runnable, period, false, exceptionHandler));
    }


    public void start() {
        //启动时间监视器
        monitor.startRunning();
    }


}
