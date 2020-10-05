package com.schedule.simplejob.config;

import com.schedule.simplejob.timer.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-01 15:31
 **/
@Slf4j
@MapperScan(basePackages = {"com.schedule.simplejob.mapper"})
@ComponentScan("com.schedule")
@Component
public class SimpleJobConfig {

    @Bean
    public SimpleJob simpleJob() {
        SimpleJob simpleJob = new SimpleJob();
        if (!simpleJob.isRunning()) {
            simpleJob.start();
        }
        return simpleJob;
    }


}
