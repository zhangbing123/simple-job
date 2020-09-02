package com.schedule.simplejob.api;

import com.schedule.simplejob.exchandler.StopTaskExceptionHandler;
import com.schedule.simplejob.reqregister.RegisterTask;
import com.schedule.simplejob.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.timer.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${is.persistence:false}")
    private boolean isPersistence;

    /**
     * 通过http调用执行任务
     *
     * @param registerTask
     * @return 任务id
     */
    @PostMapping("/httpTask")
    public String httpTask(@RequestBody RegisterTaskForHttp registerTask) {

        return registerTask(registerTask, registerTask.createTask());
    }

    /**
     * 通过bean和method的执行任务
     *
     * @param registerTask
     * @return taskId 任务id
     */
    @PostMapping("/beanTask")
    public String httpTask(@RequestBody RegisterTaskForBean registerTask) {

        return registerTask(registerTask, registerTask.createTask());
    }

    private String registerTask(RegisterTask registerTask, Runnable task) {

        if (!StringUtils.isEmpty(registerTask.getCron())) { // 基于cron表达式的周期任务
            return simpleJob.registerByCron(registerTask.getCron(), task);
        }

        String taskId = null;
        if (registerTask.isPeriod()) {
            //周期任务
            taskId = simpleJob.registerWithFixedDelay(registerTask.getTime(), registerTask.getPeriodT(), task, new StopTaskExceptionHandler());
        } else {
            taskId = simpleJob.registerAtTime(registerTask.getTime(), task);
        }

        if (isPersistence) {
            //持久化

        } else {
            //本地缓存

        }

        return taskId;
    }
}
