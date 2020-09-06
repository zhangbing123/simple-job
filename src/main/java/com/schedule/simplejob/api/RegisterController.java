package com.schedule.simplejob.api;

import com.schedule.simplejob.model.StatisticalExecModel;
import com.schedule.simplejob.model.UpdateTaskInfo;
import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.model.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.model.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.result.Result;
import com.schedule.simplejob.service.JobService;
import com.schedule.simplejob.timer.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        simpleJob.stop(taskInfo.getTaskId());

        return Result.ok(null, "操作成功");
    }

    /**
     * 查询任务列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<Job>> getList(@RequestParam("page") int page, @RequestParam("limit") int limit) {

        return Result.ok(jobService.list(), "查询成功");
    }

    /**
     * 查询任务执行情况
     *
     * @return
     */
    @GetMapping("/statistical")
    public Result<List<StatisticalExecModel>> getStatisticalData(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return null;
    }


}
