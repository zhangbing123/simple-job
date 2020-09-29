package com.schedule.simplejob.test;

import com.alibaba.fastjson.JSON;
import com.schedule.simplejob.timer.SimpleJob;
import com.schedule.simplejob.timer.TimeTaskRunner;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-03 10:24
 **/
public class TaskSerializeTest {

    public static void main(String[] args) {
        SimpleJob simpleJob = new SimpleJob();
        TimeTaskRunner task = new TimeTaskRunner(simpleJob, new RunTask("testThread"), -1);

        String string = JSON.toJSONString(task);
        System.out.println(string);
        TimeTaskRunner timeRunTask = JSON.parseObject(string, TimeTaskRunner.class);
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
