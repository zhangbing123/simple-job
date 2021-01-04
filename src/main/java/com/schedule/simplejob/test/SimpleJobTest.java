package com.schedule.simplejob.test;

import com.schedule.simplejob.timer.SimpleJob;
import com.schedule.simplejob.utils.DateUtil;

import java.util.Date;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-08-25 14:46
 **/
public class SimpleJobTest {

    private static SimpleJob simpleJob = new SimpleJob();

    public static void main(String[] args) throws InterruptedException {

        Date date = DateUtil.parseDate("2021-01-04 16:04:00");
        simpleJob.start();
        simpleJob.registerAtTime(date.getTime(), new RunTask("线程0"),"123",true);
//        simpleJob.registerAtFixedRate(1000, 2000, new RunTask("线程1"));
//        simpleJob.registerWithFixedDelay(1000, 2000, new RunTask("线程2"));

//        simpleJob.registerByCron("*/1 * * * * ?", new RunTask2("线程3"), null);


//        Thread.currentThread().sleep(1000);
//
//        new Thread(() -> {
//            String time1 = "2020-08-31 11:03:04";
//            Date date1 = DateUtil.parseDate(time1);
////            simpleJob.registerAtTime(date1.getTime(), new RunTask("线程1"));
//            simpleJob.registerAtFixedRate(1000,1000, new RunTask("线程1"));
//        }).start();
//
//        new Thread(() -> {
//            String time2 = "2020-08-31 10:55:04";
//            Date date2 = DateUtil.parseDate(time2);
//            simpleJob.register(date2.getTime(), new RunTask("线程2"));
//        }).start();
//
//        new Thread(() -> {
//            String time3 = "2020-08-31 10:55:06";
//            Date date3 = DateUtil.parseDate(time3);
//            simpleJob.register(date3.getTime(), new RunTask("线程3"));
//        }).start();
//
//        new Thread(() -> {
//            String time4 = "2020-08-31 10:55:08";
//            Date date4 = DateUtil.parseDate(time4);
//            simpleJob.register(date4.getTime(), new RunTask("线程4"));
//        }).start();
//
//        String timeMain = "2020-08-27 20:40:16";
//        Date dateMain = DateUtil.parseDate(timeMain);
//        simpleJob.register(dateMain.getTime(), new RunTask("主线程"));
//        simpleJob.start();


    }


    static class RunTask implements Runnable {

        private String threadName;

        public RunTask(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            System.out.println(threadName + "任务开始执行...");
            try {
                Thread.currentThread().sleep(4000);
            } catch (InterruptedException e) {

            }
//            System.out.println(1 / 0);
            System.out.println(threadName + "任务执行结束....");
//            System.out.println(threadName + "到时间了，触发事件...");
        }
    }

    static class RunTask2 implements Runnable {

        private String threadName;

        public RunTask2(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            System.out.println(threadName + "任务执行....");
//            System.out.println(threadName + "到时间了，触发事件...");
        }
    }
}
