package com.schedule.simplejob.exchandler;

/**
 * @description: 任务异常处理策略 直接抛出异常
 * @author: zhangbing
 * @create: 2020-08-31 17:25
 **/
public class DefaultTaskExceptionHandler implements TaskExceptionHandler {

    @Override
    public void handle(Exception e,Runnable runnable) {
        System.out.println("发生异常：本次任务丢弃....");
    }
}
