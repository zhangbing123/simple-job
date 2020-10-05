package com.schedule.simplejob.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.schedule.simplejob.curd.BaseMapper;
import com.schedule.simplejob.curd.BaseServiceImpl;
import com.schedule.simplejob.exchandler.StopTaskExceptionHandler;
import com.schedule.simplejob.mapper.JobMapper;
import com.schedule.simplejob.model.entity.ExecuteJob;
import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.model.req.QueryReq;
import com.schedule.simplejob.model.reqregister.RegisterTask;
import com.schedule.simplejob.model.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.model.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.service.ExecuteJobService;
import com.schedule.simplejob.service.JobService;
import com.schedule.simplejob.timer.SimpleJob;
import com.schedule.simplejob.timer.TimeRunTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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

    @Autowired
    private ExecuteJobService executeJobService;

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
                .periodTime(registerTask.getPeriodTime())
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
    public PageInfo<Job> list(QueryReq queryReq) {
        PageHelper.startPage(queryReq.getPage(), queryReq.getLimit());
        List<Job> jobList = jobMapper.selectParam();
        if (CollectionUtils.isEmpty(jobList)) {
            return new PageInfo<>();
        }

        return new PageInfo<>(jobList);
    }

    @Override
    public String registerTaskAndPersist(RegisterTask registerTask) {

        TimeRunTask timeRunTask = registerTaskNotPersist(registerTask);

        if (isPersistence) {
            //持久化
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
                        registerTask.getPeriodTime(),
                        task,
                        new StopTaskExceptionHandler(), registerTask.getTaskId(), registerTask.isStatistical());

            } else {
                timeRunTask = simpleJob.registerAtTime(registerTask.getTime(), task, registerTask.getTaskId(), registerTask.isStatistical());
            }
        } else {
            // 基于cron表达式的周期任务
            timeRunTask = simpleJob.registerByCron(registerTask.getCron(), task, null, registerTask.getTaskId(), registerTask.isStatistical());
        }
        return timeRunTask;
    }

    @Override
    public boolean reRegister(Job job) {
        RegisterTask task = null;

        if (job.getIsPeriod() == 0 && job.getTime() < System.currentTimeMillis()) {
            //非周期任务 且任务执行时间已过的  不在执行
            return false;
        }

        if ("HTTP".equals(job.getType())) {
            task = RegisterTaskForHttp.builder()
                    .url(job.getUrl())
                    .httpMethod(job.getHttpMethod())
                    .build();


        } else if ("BEAN".equals(job.getType())) {

            task = RegisterTaskForBean.builder()
                    .beanName(job.getBeanName())
                    .methodName(job.getMethodName())
                    .build();

        }

        task.setArgs(job.getArgs());
        task.setCron(job.getCron());
        task.setPeriodTime(job.getPeriodTime());
        task.setPeriod(job.getIsPeriod() == 1);
        task.setTime(job.getTime());
        task.setTaskId(job.getId());
        task.setStatistical(true);
        this.registerTaskNotPersist(task);
        return true;
    }

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    @Override
    public int delTask(String taskId) {
        executeJobService.delete(ExecuteJob.builder().jobId(taskId).build());
        return super.deleteByPrimaryKey(taskId);
    }

    @Override
    public boolean stop(String taskId) {
        simpleJob.stop(taskId);
        int i = super.updateByPrimaryKeySelective(Job.builder().id(taskId).status("STOP").build());
        return i == 1;
    }


}
