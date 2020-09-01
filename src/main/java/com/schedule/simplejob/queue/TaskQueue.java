package com.schedule.simplejob.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @description: 封装一个任务队列
 * @author: zhangbing
 * @create: 2020-08-31 09:32
 **/
public class TaskQueue {

    private TreeMap<Long, List<Runnable>> taskQueue = new TreeMap();

    public void addQueue(Long time, Runnable runnable) {
        List<Runnable> runnables = taskQueue.get(time);
        if (runnables != null) {
            runnables.add(runnable);
        } else {
            runnables = new ArrayList<>();
            runnables.add(runnable);
            taskQueue.put(time, runnables);
        }
    }

    public long getTime() {
        if (!isEmpty()) {
            return taskQueue.firstKey();
        }

        return -1;

    }

    public List<Runnable> getTaskAndRmv() {
        long time = getTime();
        List<Runnable> runnables = taskQueue.get(time);
        taskQueue.remove(time);
        return runnables;
    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

}
