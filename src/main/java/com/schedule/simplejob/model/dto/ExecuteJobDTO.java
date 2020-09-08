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
public class ExecuteJobDTO {

    private Long id;

    private String jobId;

    private String status;

    private String exception;

    private long excuteDate;

    private Date createTime;

    private Date modifyTime;

    private String name;

}