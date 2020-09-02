package com.schedule.simplejob.timer;

import com.schedule.simplejob.exchandler.DefaultTaskExceptionHandler;
import com.schedule.simplejob.exchandler.TaskExceptionHandler;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;
import java.util.UUID;

/**
 * @description: 真正执行的任务对象
 * @author: zhangbing
 * @create: 2020-08-31 16:12
 **/
public class TimeRunTask implements Runnable {

    private String taskId;

    private SimpleJob simpleJob;

    private Runnable runnable;

    private CronSequenceGenerator cronSequenceGenerator;//cron表达式解析器

    /**
     * -1表示不是周期任务 一次性任务
     * -2表示是通过cron表达式执行的任务
     */
    private long period;

    private boolean isDelay;

    private TaskExceptionHandler exceptionHandler;

    public TimeRunTask(SimpleJob simpleJob, Runnable runnable, CronSequenceGenerator cronSequenceGenerator, TaskExceptionHandler exceptionHandler) {
        UUID uuid = UUID.randomUUID();
        this.taskId = uuid.toString();
        this.simpleJob = simpleJob;
        this.runnable = runnable;
        this.cronSequenceGenerator = cronSequenceGenerator;
        this.period = -2;
        this.exceptionHandler = exceptionHandler == null ? new DefaultTaskExceptionHandler() : exceptionHandler;
    }

    public TimeRunTask(SimpleJob simpleJob, Runnable runnable, long period) {
        this(simpleJob, runnable, period, false, null);
    }

    public TimeRunTask(SimpleJob simpleJob, Runnable runnable, long period, TaskExceptionHandler exceptionHandler) {
        this(simpleJob, runnable, period, true, exceptionHandler);
    }

    public TimeRunTask(SimpleJob simpleJob, Runnable runnable, long period, boolean isDelay, TaskExceptionHandler exceptionHandler) {
        this.simpleJob = simpleJob;
        this.runnable = runnable;
        this.period = period;
        this.isDelay = isDelay;
        this.exceptionHandler = exceptionHandler == null ? new DefaultTaskExceptionHandler() : exceptionHandler;
        UUID uuid = UUID.randomUUID();
        this.taskId = uuid.toString();
    }

    @Override
    public void run() {
        Date currentDate = new Date();
        long startTime = currentDate.getTime();
        try {
            //执行任务
            runnable.run();

            //todo 执行成功 统计执行情况

        } catch (Exception e) {

            //todo 执行失败 统计执行情况

            //进入异常处理逻辑
            exceptionHandler.handle(e, this);
        }

        long nextTime = 0L;
        if (period >= 0) {//周期任务
            if (isDelay) {
                nextTime = System.currentTimeMillis() + period;

            } else {
                nextTime = startTime + period;
            }
        } else if (period == -2) {
            Date next = cronSequenceGenerator.next(currentDate);
            nextTime = next.getTime();
        }

        if (nextTime > 0)
            simpleJob.register(nextTime, this);
    }

    public String getTaskId() {
        return taskId;
    }


}
