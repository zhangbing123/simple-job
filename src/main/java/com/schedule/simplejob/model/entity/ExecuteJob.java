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
@Table(name = "t_execute_job")
public class ExecuteJob {

    @Id
    private Long id;

    @Column(name = "job_id")
    private String jobId;

    private String status;

    private String exception;

    @Column(name = "excute_date")
    private long excuteDate;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

}