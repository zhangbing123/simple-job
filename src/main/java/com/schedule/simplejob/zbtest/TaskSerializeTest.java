package com.schedule.simplejob.zbtest;

import com.alibaba.fastjson.JSON;
import com.schedule.simplejob.timer.SimpleJob;
import com.schedule.simplejob.timer.TimeRunTask;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-03 10:24
 **/
public class TaskSerializeTest {

    public static void main(String[] args) {
        SimpleJob simpleJob = new SimpleJob();
        TimeRunTask task = new TimeRunTask(simpleJob, new RunTask("testThread"), -1);

        String string = JSON.toJSONString(task);
        System.out.println(string);
        TimeRunTask timeRunTask = JSON.parseObject(string, TimeRunTask.class);
        new Thread(timeRunTask).start();
    }

    static class RunTask implements Runnable {

        private String threadName;

        public RunTask(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            System.out.println(threadName + "任务执行....");
        }
    }
}
