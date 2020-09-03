package com.schedule.simplejob.timer;

import com.schedule.simplejob.component.LocalCache;
import com.schedule.simplejob.exchandler.DefaultTaskExceptionHandler;
import com.schedule.simplejob.exchandler.TaskExceptionHandler;
import com.schedule.simplejob.model.StatisticalExecModel;
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

    private boolean isCancel;

    private boolean isStatistical;

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public void setStatistical(boolean statistical) {
        isStatistical = statistical;
    }

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

        if (isCancel) return;

        Date currentDate = new Date();

        try {
            //执行任务
            runnable.run();

            //执行成功 统计执行情况
            statistical(true, null);


        } catch (Exception e) {

            //执行失败 统计执行情况
            statistical(false, e.getMessage());

            //进入异常处理逻辑
            exceptionHandler.handle(e, this);
        }

        this.handlePeriod(currentDate);
    }

    //统计任务执行情况
    private void statistical(boolean isSuccessful, String exception) {

        if (!isStatistical) return;//无需统计

        Date date = new Date();
        StatisticalExecModel statisticalExecModel = StatisticalExecModel.builder()
                .taskId(taskId)
                .isSuccessful(isSuccessful)
                .excuteDate(date)
                .exception(exception)
                .build();
        LocalCache.getInstance().addCache(StatisticalExecModel.STATISTICAL_DATA + taskId + ":time=" + date.getTime(), statisticalExecModel);
    }

    private void handlePeriod(Date currentDate) {

        long startTime = currentDate.getTime();//任务执行前的时间

        long nextTime = 0L;
        if (period >= 0) {//周期任务
            if (isDelay) {
                //任务执行完成后的当前时间 + 任务循环周期
                nextTime = System.currentTimeMillis() + period;

            } else {
                nextTime = startTime + period;
            }
        } else if (period == -2) {//通过cron注册进来的周期任务
            Date next = cronSequenceGenerator.next(currentDate);
            nextTime = next.getTime();
        }

        if (nextTime > 0)
            simpleJob.register(nextTime, this);
    }

    public String getTaskId() {
        return taskId;
    }


    public void check() {

        if (runnable == null) throw new RuntimeException("the task is null");
    }
}
