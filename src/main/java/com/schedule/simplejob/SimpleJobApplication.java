package com.schedule.simplejob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@MapperScan(basePackages = {"com.schedule.simplejob.mapper"})
@SpringBootApplication
public class SimpleJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleJobApplication.class, args);
        log.info("server start successfully！！！");
    }

}
