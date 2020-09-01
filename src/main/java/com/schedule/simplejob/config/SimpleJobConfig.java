package com.schedule.simplejob.config;

import com.schedule.simplejob.timer.SimpleJob;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-01 15:31
 **/
@Component
public class SimpleJobConfig {

    @Bean
    public SimpleJob simpleJob() {
        SimpleJob simpleJob = new SimpleJob();
        simpleJob.start();
        return simpleJob;
    }
}
