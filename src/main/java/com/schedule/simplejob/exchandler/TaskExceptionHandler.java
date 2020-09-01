package com.schedule.simplejob.exchandler;

/**
 * @description: 任务执行异常处理 策略
 * @author: zhangbing
 * @create: 2020-08-31 17:24
 **/
public interface TaskExceptionHandler {

    void handle(Exception e, Runnable runnable);
}
