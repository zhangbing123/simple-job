package com.schedule.simplejob.service.impl;

import com.schedule.simplejob.curd.BaseMapper;
import com.schedule.simplejob.curd.BaseServiceImpl;
import com.schedule.simplejob.exchandler.StopTaskExceptionHandler;
import com.schedule.simplejob.mapper.JobMapper;
import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.model.reqregister.RegisterTask;
import com.schedule.simplejob.model.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.model.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.service.JobService;
import com.schedule.simplejob.timer.SimpleJob;
import com.schedule.simplejob.timer.TimeRunTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description: 任务持久化服务
 * @Author: zhangbing
 * @CreateDate: 2020/9/6 2:27 PM
 */
@Transactional
@Service
public class JobServiceImpl extends BaseServiceImpl<Job, String> implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private SimpleJob simpleJob;

    @Value("${is.persistence:false}")
    private boolean isPersistence;


    @Override
    public BaseMapper<Job, String> getMapper() {
        return jobMapper;
    }


    @Override
    public Job save(RegisterTask registerTask, String taskId) {

        Date date = new Date();

        Job job = Job.builder()
                .id(taskId)
                .args(registerTask.getArgs())
                .cron(registerTask.getCron())
                .description(registerTask.getDesc())
                .isPeriod(registerTask.isPeriod() ? 1 : 0)
                .periodTime(registerTask.getPeriodT())
                .time(registerTask.getTime())
                .name(registerTask.getName())
                .status("RUNNING")
                .createTime(date)
                .modifyTime(date)
                .build();

        if (registerTask instanceof RegisterTaskForHttp) {

            RegisterTaskForHttp taskForHttp = (RegisterTaskForHttp) registerTask;

            job.setUrl(taskForHttp.getUrl());
            job.setHttpMethod(taskForHttp.getHttpMethod());
            job.setType("HTTP");

        } else {

            RegisterTaskForBean taskForBean = (RegisterTaskForBean) registerTask;

            job.setBeanName(taskForBean.getBeanName());
            job.setMethodName(taskForBean.getMethodName());
            job.setType("BEAN");
        }
        super.insert(job);
        return job;
    }

    @Override
    public List<Job> list() {
        return selectAll();
    }

    @Override
    public String registerTaskAndPersist(RegisterTask registerTask) {

        TimeRunTask timeRunTask = registerTaskNotPersist(registerTask);

        if (isPersistence) {
            //需要进行数据统计
            timeRunTask.setStatistical(true);
            //入本地缓存  假的持久化
            this.save(registerTask, timeRunTask.getTaskId());
        }


        return timeRunTask.getTaskId();
    }

    @Override
    public TimeRunTask registerTaskNotPersist(RegisterTask registerTask) {
        Runnable task = registerTask.createTask();

        TimeRunTask timeRunTask = null;

        if (StringUtils.isEmpty(registerTask.getCron())) {
            if (registerTask.isPeriod()) {
                //周期任务
                timeRunTask = simpleJob.registerWithFixedDelay(registerTask.getTime(),
                        registerTask.getPeriodT(),
                        task,
                        new StopTaskExceptionHandler());

            } else {
                timeRunTask = simpleJob.registerAtTime(registerTask.getTime(), task);
            }
        } else {
            timeRunTask = simpleJob.registerByCron(registerTask.getCron(), task);// 基于cron表达式的周期任务
        }
        return timeRunTask;
    }


}