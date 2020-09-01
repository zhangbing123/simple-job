package com.schedule.simplejob.timer;

import com.schedule.simplejob.exchandler.TaskExceptionHandler;
import com.schedule.simplejob.queue.TaskQueue;
import com.schedule.simplejob.utils.UnsafeInstance;
import sun.misc.Unsafe;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-08-31 16:02
 **/
public class SimpleJob {

    private TaskQueue queue = new TaskQueue();
    private ThreadPoolExecutor executor;
    private volatile int state;
    private static Unsafe unsafe;
    private static long stateOffset;
    private Thread monitorThread;

    static {

        //通过反射获取Unsafe类 此类中的cas原子操作实现方式主要基于cmpxchg指令实现的
        unsafe = UnsafeInstance.reflectGetUnsafe();

        //获取对象属性的偏移量（在内存中的位置）
        try {
            stateOffset = unsafe.objectFieldOffset(SimpleJob.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {

        }
    }

    public SimpleJob() {
        executor = new ThreadPoolExecutor(10, 60, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        //创建一个监控线程 用于监控时间
        monitorThread = new Thread(() -> monitorTimeTask());
    }


    /**
     * 在指定的时间执行
     *
     * @param time     执行的时间点
     * @param runnable 执行任务
     */
    public void registerAtTime(long time, Runnable runnable) {

        synchronized (queue) {

            queue.addQueue(time, runnable);

            queue.notify();
        }
    }


    /**
     * 间隔重复执行 任务执行结束开始计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerWithFixedDelay(long time, long period, Runnable runnable) {

        registerWithFixedDelay(time, period, runnable, null);
    }

    /**
     * 间隔重复执行 任务执行结束开始计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerWithFixedDelay(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler) {

        registerAtTime(System.currentTimeMillis() + time, new TimeRunTask(this, runnable, period, exceptionHandler));
    }


    /**
     * 间隔重复执行 任务执行开始时计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerAtFixedRate(long time, long period, Runnable runnable) {
        registerAtFixedRate(time, period, runnable, null);
    }

    /**
     * 间隔重复执行 任务执行开始时计时 period时间后再次执行
     *
     * @param time     单位：ms
     * @param period   单位：ms
     * @param runnable
     */
    public void registerAtFixedRate(long time, long period, Runnable runnable, TaskExceptionHandler exceptionHandler) {
        registerAtTime(System.currentTimeMillis() + time, new TimeRunTask(this, runnable, period, false, exceptionHandler));
    }


    public void start() {

        if (isRunning()) return;

        startRunning();

    }

    private void monitorTimeTask() {

        while (true) {

            synchronized (queue) {

                if (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                    }
                }

                long time = queue.getTime();

                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis >= time) {
                    runTask(queue.getTaskAndRmv());
                } else {
                    //未到触发事件  线程等待
                    try {
                        queue.wait(time - currentTimeMillis);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }
    }

    private void runTask(List<Runnable> taskAndRmv) {

        for (Runnable runnable : taskAndRmv) {
            executor.execute(runnable);
        }

    }


    private boolean startRunning() {
        if (casState()) {
            monitorThread.start();
            return true;
        }
        return false;
    }

    private boolean casState() {
        return unsafe.compareAndSwapInt(this, stateOffset, 0, 1);
    }


    private boolean isRunning() {
        if (!(state == 1 && monitorThread.isAlive())) {
            state = 0;
            return false;
        }
        return true;
    }
}
