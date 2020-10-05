package com.schedule.simplejob.timer;

import com.schedule.simplejob.exception.SimpleRunTimeException;
import com.schedule.simplejob.exchandler.TaskExceptionHandler;
import com.schedule.simplejob.queue.TaskQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

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
     * @return TimeRunTask  任务
     */
    public TimeRunTask register(long time, TimeRunTask runnable) {

        if (time < 0) throw new SimpleRunTimeException("the time is null");

        runnable.check();

        synchronized (queue) {

            queue.addQueue(time, runnable);

            queue.notify();

            log.info("task registration successful");
            return runnable;
        }
    }


    /**
     * 在指定的时间执行
     *
     * @param time     ms    执行的时间点
     * @param runnable 执行任务
     */
    public TimeRunTask registerAtTime(long time, Runnable runnable) {
        return registerAtTime(time, runnable, null, false);
    }

    public TimeRunTask registerAtTime(long time, Runnable runnable, String taskId, boolean isStatistical) {
        return register(time, new TimeRunTask(this, runnable, -1, taskId, isStatistical));
    }


    /**
     * 间隔重复执行 任务执行结束开始计时 period时间后再次执行
     *
     * @param time     单位：ms 多少ms之后执行
     * @param period   单位：ms 多少ms执行一次
     * @param runnable
     */
    public TimeRunTask registerWithFixedDelay(long time, long period, Runnable runnable) {

        return registerWithFixedDelay(time, period, runnable, null);
    }

    public TimeRunTask registerWithFixedDelay(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler) {
        return registerWithFixedDelay(time, period, runnable, exceptionHandler, null, false);
    }

    public TimeRunTask registerWithFixedDelay(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler, String taskId, boolean isStatistical) {
        return register(System.currentTimeMillis() + time,
                new TimeRunTask(this,
                        runnable,
                        period,
                        exceptionHandler, taskId, isStatistical));
    }


    /**
     * 间隔重复执行 任务执行开始时计时 period时间后再次执行
     *
     * @param time     单位：ms 多少ms之后执行
     * @param period   单位：ms 多少ms执行一次
     * @param runnable
     */
    public TimeRunTask registerAtFixedRate(long time, long period, Runnable runnable) {
        return registerAtFixedRate(time, period, runnable, null);
    }

    public TimeRunTask registerAtFixedRate(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler) {

        return registerAtFixedRate(time, period, runnable, exceptionHandler, null, false);

    }

    public TimeRunTask registerAtFixedRate(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler, String taskId, boolean isStatistical) {

        return register(System.currentTimeMillis() + time, new TimeRunTask(this, runnable, period, false, exceptionHandler, taskId, isStatistical));

    }


    /**
     * 支持cron表达式
     *
     * @param cron
     * @param runnable
     * @param
     * @return
     */
    public TimeRunTask registerByCron(String cron, Runnable runnable) {
        return registerByCron(cron, runnable, null);
    }

    public TimeRunTask registerByCron(String cron, Runnable runnable, TaskExceptionHandler exceptionHandler) {
        return registerByCron(cron, runnable, exceptionHandler, null, false);
    }

    public TimeRunTask registerByCron(String cron, Runnable runnable, TaskExceptionHandler exceptionHandler, String taskId, boolean isStatistical) {
        try {

            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
            Date next = cronSequenceGenerator.next(new Date());
            return register(next.getTime(), new TimeRunTask(this, runnable, cronSequenceGenerator, exceptionHandler, taskId, isStatistical));

        } catch (Exception e) {
            throw new SimpleRunTimeException("the cron expression format error, please check");
        }
    }


    /**
     * 启动定时器
     */
    public void start() {
        //启动时间监视器
        monitor.startRunning();
    }

    /**
     * 定时器是否启动
     *
     * @return
     */
    public boolean isRunning() {
        return monitor.isRunning();
    }

    /**
     * 停止任务
     *
     * @param taskId
     */
    public boolean stop(String taskId) {
        synchronized (queue) {
            return queue.remove(taskId);
        }
    }


}
