package com.schedule.simplejob.service;

import com.github.pagehelper.PageInfo;
import com.schedule.simplejob.curd.BaseService;
import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.model.req.QueryReq;
import com.schedule.simplejob.model.reqregister.RegisterTask;
import com.schedule.simplejob.timer.TimeRunTask;

public interface JobService extends BaseService<Job, String> {


    /**
     * 保存任务
     *
     * @param registerTask
     * @param taskId
     * @return
     */
    Job save(RegisterTask registerTask, String taskId);


    /**
     * 查询任务集合
     *
     * @return
     */
    PageInfo<Job> list(QueryReq queryReq);


    /**
     * 任务注册 并持久化到库
     *
     * @param registerTask
     * @return
     */
    String registerTaskAndPersist(RegisterTask registerTask);


    TimeRunTask registerTaskNotPersist(RegisterTask registerTask);

    /**
     * 持久化的任务  重新启动
     *
     * @param job
     * @return
     */
    boolean reRegister(Job job);

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    int delTask(String taskId);

    /**
     * 禁用任务
     * @param taskId
     * @return
     */
    boolean stop(String taskId);

}
