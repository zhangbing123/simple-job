package com.schedule.simplejob.queue;

import com.schedule.simplejob.timer.TimeTaskRunner;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @description: 封装一个任务队列
 * @author: zhangbing
 * @create: 2020-08-31 09:32
 **/
public class TaskQueue {

    private TreeMap<Long, List<TimeTaskRunner>> taskQueue = new TreeMap();

    public void addQueue(Long time, TimeTaskRunner runnable) {
        List<TimeTaskRunner> runnables = taskQueue.get(time);
        if (runnables != null) {
            runnables.add(runnable);
        } else {
            runnables = new ArrayList<>();
            runnables.add(runnable);
            taskQueue.put(time, runnables);
        }
    }

    public void addQueues(Long time, List<TimeTaskRunner> runnables) {
        if (CollectionUtils.isEmpty(runnables)) {
            return;
        }
        List<TimeTaskRunner> oldRunnables = taskQueue.get(time);
        if (!CollectionUtils.isEmpty(oldRunnables)) {
            runnables.addAll(runnables);
        } else {
            taskQueue.put(time, runnables);
        }
    }

    public long getTime() {
        if (!isEmpty()) {
            return taskQueue.firstKey();
        }

        return -1;

    }

    public List<TimeTaskRunner> getTaskAndRmv() {
        long time = getTime();
        List<TimeTaskRunner> runnables = taskQueue.get(time);
        taskQueue.remove(time);
        return runnables;
    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

    //取消任务 不在执行任务
    public boolean remove(String taskId) {
        Set<Map.Entry<Long, List<TimeTaskRunner>>> entries = taskQueue.entrySet();
        for (Map.Entry<Long, List<TimeTaskRunner>> entry : entries) {
            List<TimeTaskRunner> timeRunTasks = entry.getValue();
            if (!CollectionUtils.isEmpty(timeRunTasks)) {
                for (TimeTaskRunner timeRunTask : timeRunTasks) {
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
