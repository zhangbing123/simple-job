package com.schedule.simplejob.timer;

import com.schedule.simplejob.queue.TaskQueue;
import com.schedule.simplejob.utils.UnsafeInstance;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.util.List;

/**
 * @description: 时间监视器
 * @author: zhangbing
 * @create: 2020-09-02 10:35
 **/
@Slf4j
public class TimeMonitor {

    private TaskQueue queue;

    private Thread monitorThread;

    private volatile int state;

    private static Unsafe unsafe;

    private static long stateOffset;

    private TaskExecutor executor;

    static {

        //通过反射获取Unsafe类 此类中的cas原子操作实现方式主要基于cmpxchg指令实现的
        unsafe = UnsafeInstance.reflectGetUnsafe();

        //获取对象属性的偏移量（在内存中的位置）
        try {
            stateOffset = unsafe.objectFieldOffset(TimeMonitor.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {

        }
    }

    public TimeMonitor(TaskQueue queue) {
        executor = new TaskExecutor();
        //创建一个监控线程 用于监控时间
        this.monitorThread = new Thread(() -> monitorTimeTask());
        this.queue = queue;
    }


    public boolean startRunning() {

        if (isRunning()) return true;

        if (casState()) {
            monitorThread.start();
            log.info("the simpleJob start successfully");
            return true;
        }
        return false;
    }

    private void monitorTimeTask() {

        while (true) {

            synchronized (queue) {

                if (queue.isEmpty()) {//队列为空
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {

                    }
                }

                long time = queue.getFirstTime();

                long currentTimeMillis = System.currentTimeMillis();
                if (time < 0 || currentTimeMillis >= time) {
                    //执行任务  执行失败重新入队
                    queue.addQueues(time, executor.runTask(queue.getTaskAndRmv()));
                } else {
                    //未到触发时间  线程等待
                    try {
                        queue.wait(time - currentTimeMillis);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }
    }


    private boolean casState() {
        return unsafe.compareAndSwapInt(this, stateOffset, 0, 1);
    }

    public boolean isRunning() {
        if (!(state == 1 && monitorThread.isAlive())) {
            state = 0;
            return false;
        }
        return true;
    }

}
