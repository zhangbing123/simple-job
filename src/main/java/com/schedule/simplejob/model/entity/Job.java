package com.schedule.simplejob.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "t_job")
public class Job {

    @Id
    private String id;

    private String name;

    private String type;

    private String url;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "is_period")
    private boolean isPeriod;

    @Column(name = "period_time")
    private long periodTime;

    private long time;

    private String args;

    private String desc;

    private String cron;

    private String status;

    private int deleted;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

}
