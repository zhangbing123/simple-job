package com.schedule.simplejob.config;

import com.schedule.simplejob.timer.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-01 15:31
 **/
@Slf4j
@Component
public class SimpleJobConfig {

    @Bean
    public SimpleJob simpleJob() {
        SimpleJob simpleJob = new SimpleJob();
        simpleJob.start();
        return simpleJob;
    }
}
