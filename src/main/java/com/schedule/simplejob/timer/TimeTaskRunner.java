package com.schedule.simplejob.timer;

import com.schedule.simplejob.exception.SimpleRunTimeException;
import com.schedule.simplejob.exchandler.TaskExceptionHandler;
import com.schedule.simplejob.service.ExecuteJobService;
import com.schedule.simplejob.service.impl.ExecuteJobServiceImpl;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @description: 真正执行的任务对象
 * @author: zhangbing
 * @create: 2020-08-31 16:12
 **/
public class TimeTaskRunner implements Runnable {

    private String taskId;

    private SimpleJob simpleJob;

    private Runnable runnable;

    private CronSequenceGenerator cronSequenceGenerator;//cron表达式解析器

    /**
     * -1表示不是周期任务 一次性任务
     * -2表示是通过cron表达式执行的任务
     */
    private long period;

    /**
     * 周期任务 距离下一次执行的时间计算是从本次任务执行开始时计时  还是从本次任务执行结束开始计时？
     */
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

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, CronSequenceGenerator cronSequenceGenerator, TaskExceptionHandler exceptionHandler) {
        this(simpleJob, runnable, cronSequenceGenerator, exceptionHandler, null, false);
    }

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, CronSequenceGenerator cronSequenceGenerator, TaskExceptionHandler exceptionHandler, String taskId, boolean isStatistical) {
        this(taskId, simpleJob, runnable, cronSequenceGenerator, -2, false, exceptionHandler, isStatistical);
    }

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, long period) {
        this(simpleJob, runnable, period, false, null);
    }

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, long period, String taskId, boolean isStatistical) {
        this(simpleJob, runnable, period, false, null, taskId, isStatistical);
    }

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, long period, TaskExceptionHandler exceptionHandler) {
        this(simpleJob, runnable, period, true, exceptionHandler);
    }

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, long period, TaskExceptionHandler exceptionHandler, String taskId, boolean isStatistical) {
        this(taskId, simpleJob, runnable, null, period, false, exceptionHandler, isStatistical);
    }

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, long period, boolean isDelay, TaskExceptionHandler exceptionHandler) {
        this(simpleJob, runnable, period, isDelay, exceptionHandler, null, false);
    }

    public TimeTaskRunner(SimpleJob simpleJob, Runnable runnable, long period, boolean isDelay, TaskExceptionHandler exceptionHandler, String taskId, boolean isStatistical) {
        this(taskId, simpleJob, runnable, null, period, isDelay, exceptionHandler, isStatistical);
    }

    public TimeTaskRunner(String taskId, SimpleJob simpleJob, Runnable runnable, CronSequenceGenerator cronSequenceGenerator, long period, boolean isDelay, TaskExceptionHandler exceptionHandler, boolean isStatistical) {
        this.taskId = StringUtils.isEmpty(taskId) ? UUID.randomUUID().toString() : taskId;
        this.simpleJob = simpleJob;
        this.runnable = runnable;
        this.cronSequenceGenerator = cronSequenceGenerator;
        this.period = period;
        this.isDelay = isDelay;
        this.exceptionHandler = exceptionHandler;
        this.isStatistical = isStatistical;
    }

    @Override
    public void run() {

        if (isCancel) return;

        Date currentDate = new Date();
        long runPreTime = currentDate.getTime();

        String exception = null;
        try {
            //执行任务
            runnable.run();

        } catch (Exception e) {

            exception = e.getMessage();

            //进入异常处理逻辑
            exceptionHandler.handle(e, this);
        }

        //执行完成 统计执行情况
        this.statistical(true, exception, System.currentTimeMillis() - runPreTime);

        this.handlePeriod(currentDate);
    }

    //统计任务执行情况
    private void statistical(boolean isSuccessful, String exception, long execuTime) {

        if (!isStatistical) return;//无需统计

        ExecuteJobService executeJobService = ExecuteJobServiceImpl.getInstance();

        if (isSuccessful) {
            executeJobService.saveSuccessful(execuTime, taskId);
        } else {
            executeJobService.saveFail(execuTime, taskId, exception);
        }
    }

    //处理周期任务
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

        if (runnable == null) throw new SimpleRunTimeException("the task is null");
    }
}
