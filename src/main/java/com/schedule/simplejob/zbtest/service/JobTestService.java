package com.schedule.simplejob.zbtest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-01 16:14
 **/
@Slf4j
@Service
public class JobTestService {

    public void test1() {
        System.out.println(1 / 0);
        log.info("test1方法已执行。。。");
    }

    public void test2(String s) {
        log.info("test1方法已执行。。。 参数：" + s);
    }
}
