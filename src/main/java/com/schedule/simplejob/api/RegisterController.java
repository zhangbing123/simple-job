package com.schedule.simplejob.api;

import com.github.pagehelper.PageInfo;
import com.schedule.simplejob.model.UpdateTaskInfo;
import com.schedule.simplejob.model.dto.ExecuteJobDTO;
import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.model.req.QueryReq;
import com.schedule.simplejob.model.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.model.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.result.Result;
import com.schedule.simplejob.service.ExecuteJobService;
import com.schedule.simplejob.service.JobService;
import com.schedule.simplejob.timer.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-01 15:48
 **/
@RequestMapping("/register")
@RestController
public class RegisterController {

    @Autowired
    private SimpleJob simpleJob;

    @Autowired
    private JobService jobService;

    @Autowired
    private ExecuteJobService executeJobService;


    /**
     * 通过http调用执行任务
     *
     * @param registerTask
     * @return 任务id
     */
    @PostMapping("/httpTask")
    public Result<String> httpTask(@RequestBody RegisterTaskForHttp registerTask) {

        return Result.ok(jobService.registerTaskAndPersist(registerTask), "操作成功");
    }

    /**
     * 通过bean和method的执行任务
     *
     * @param registerTask
     * @return taskId 任务id
     */
    @PostMapping("/beanTask")
    public Result<String> httpTask(@RequestBody RegisterTaskForBean registerTask) {

        return Result.ok(jobService.registerTaskAndPersist(registerTask), "操作成功");
    }

    /**
     * 停止禁用任务的执行
     *
     * @param taskInfo
     * @return
     */
    @PostMapping("/stop")
    public Result stopTask(@RequestBody UpdateTaskInfo taskInfo) {

        if (StringUtils.isEmpty(taskInfo.getTaskId())) throw new RuntimeException("the taskId is null");

        if (simpleJob.stop(taskInfo.getTaskId())) {
            jobService.updateByPrimaryKeySelective(Job.builder().id(taskInfo.getTaskId()).status("STOP").build());
        }

        return Result.ok(null, "操作成功");
    }

    /**
     * 重启任务
     *
     * @param taskInfo
     * @return
     */
    @PostMapping("/reStart")
    public Result reStart(@RequestBody UpdateTaskInfo taskInfo) {

        if (StringUtils.isEmpty(taskInfo.getTaskId())) throw new RuntimeException("the taskId is null");

        Job job = jobService.selectByPrimaryKey(taskInfo.getTaskId());
        if (job == null) throw new RuntimeException("job not found");
        if (!job.getStatus().equals("STOP")) return Result.ok(null, "the job is Running");

        jobService.reRegister(job);

        return Result.ok(null, "重启成功");
    }

    /**
     * 查询任务列表
     *
     * @return
     */
    @PostMapping("/list")
    public Result<PageInfo<Job>> getList(@RequestBody QueryReq queryReq) {

        return Result.ok(jobService.list(queryReq), "查询成功");
    }

    /**
     * 查询任务执行情况
     *
     * @return
     */
    @PostMapping("/statistical")
    public Result<PageInfo<ExecuteJobDTO>> getStatisticalData(@RequestBody QueryReq queryReq) {
        return Result.ok(executeJobService.list(queryReq), "查询成功");
    }


}
