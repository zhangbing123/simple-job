package com.schedule.simplejob.timer;

import java.util.ArrayList;
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
        this(20, 60, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(100));
    }

    public List<TimeRunTask> runTask(List<TimeRunTask> taskAndRmv) {

        List<TimeRunTask> errorTasks = new ArrayList<>(taskAndRmv.size());

        for (TimeRunTask runnable : taskAndRmv) {
            try {
                execute(runnable);
            } catch (Exception e) {
                //记录到未执行的任务里面
                errorTasks.add(runnable);
            }
        }
        return errorTasks;//返回上面  重新入队 等待下次执行
    }
}
