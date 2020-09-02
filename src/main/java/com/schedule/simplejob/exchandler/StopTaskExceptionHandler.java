package com.schedule.simplejob.exchandler;

/**
 * @description: 任务异常处理策略---结束任务周期调用
 * @author: zhangbing
 * @create: 2020-08-31 17:35
 **/
public class StopTaskExceptionHandler implements TaskExceptionHandler {


    @Override
    public void handle(Exception e, Runnable runnable) {
        throw new RuntimeException("发生异常：任务结束", e);
    }
}
