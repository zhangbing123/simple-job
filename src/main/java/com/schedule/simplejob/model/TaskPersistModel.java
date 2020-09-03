package com.schedule.simplejob.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 任务持久化对象
 * @author: zhangbing
 * @create: 2020-09-03 14:36
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaskPersistModel {

    private String id;//任务id

    private String type;//BEAN:springbean方式执行的任务   HTTP:http请求任务

    private String url;

    private String httpMethod;

    private String beanName;

    private String methodName;

    protected boolean isPeriod;

    protected long time;

    protected long periodT;

    protected String args;

    protected String name;

    protected String desc;

    private String cron;//支持cron表达式

}
