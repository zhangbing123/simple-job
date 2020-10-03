package com.schedule.simplejob.queue;

import com.schedule.simplejob.timer.TimeRunTask;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @description: 封装一个任务队列
 * @author: zhangbing
 * @create: 2020-08-31 09:32
 **/
public class TaskQueue {

    private TreeMap<Long, List<TimeRunTask>> taskQueue = new TreeMap();

    public void addQueue(Long time, TimeRunTask runnable) {
        List<TimeRunTask> runnables = taskQueue.get(time);
        if (runnables != null) {
            runnables.add(runnable);
        } else {
            runnables = new ArrayList<>();
            runnables.add(runnable);
            taskQueue.put(time, runnables);
        }
    }

    public void addQueues(Long time, List<TimeRunTask> runnables) {
        if (CollectionUtils.isEmpty(runnables)) {
            return;
        }
        List<TimeRunTask> oldRunnables = taskQueue.get(time);
        if (!CollectionUtils.isEmpty(oldRunnables)) {
            runnables.addAll(runnables);
        } else {
            taskQueue.put(time, runnables);
        }
    }

    public long getFirstTime() {
        if (!isEmpty()) {
            return taskQueue.firstKey();
        }

        return -1;

    }

    public List<TimeRunTask> getTaskAndRmv() {
        long time = getFirstTime();
        List<TimeRunTask> runnables = taskQueue.get(time);
        taskQueue.remove(time);
        return runnables;
    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

    //取消任务 不在执行任务
    public boolean remove(String taskId) {
        Set<Map.Entry<Long, List<TimeRunTask>>> entries = taskQueue.entrySet();
        for (Map.Entry<Long, List<TimeRunTask>> entry : entries) {
            List<TimeRunTask> timeRunTasks = entry.getValue();
            if (!CollectionUtils.isEmpty(timeRunTasks)) {
                for (TimeRunTask timeRunTask : timeRunTasks) {
                    if (timeRunTask.getTaskId().equals(taskId)) {
                        timeRunTask.setCancel(true);
                       return true;
                    }
                }
            }
        }

        return false;

    }


}
