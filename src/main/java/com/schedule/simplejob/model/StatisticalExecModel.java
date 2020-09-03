package com.schedule.simplejob.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 任务执行统计对象
 * @author: zhangbing
 * @create: 2020-09-03 16:36
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StatisticalExecModel {

    public final static String STATISTICAL_DATA = "STATISTICAL_DATA";

    private String taskId;

    private boolean isSuccessful;

    private String exception;

    private Date excuteDate;

    private String name;//任务名称

}
