package com.schedule.simplejob.exchandler;

import lombok.extern.log4j.Log4j2;

/**
 * @description: 任务异常处理策略 直接抛出异常
 * @author: zhangbing
 * @create: 2020-08-31 17:25
 **/
@Log4j2
public class DefaultTaskExceptionHandler implements TaskExceptionHandler {

    @Override
    public void handle(Exception e, Runnable runnable) {
        log.info("发生异常：本次任务丢弃....");
    }
}
