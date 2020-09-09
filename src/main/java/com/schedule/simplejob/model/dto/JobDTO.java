package com.schedule.simplejob.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JobDTO {

    private String id;

    private String name;

    private String type;

    private String url;

    private String httpMethod;

    private String beanName;

    private String methodName;

    private Integer isPeriod;

    private Long periodTime;

    private Long time;

    private String args;

    private String description;

    private String cron;

    private String status;

    private int deleted;

    private Date createTime;

    private Date modifyTime;

}
