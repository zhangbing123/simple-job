package com.schedule.simplejob.api;

import com.schedule.simplejob.exchandler.StopTaskExceptionHandler;
import com.schedule.simplejob.reqregister.RegisterTask;
import com.schedule.simplejob.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.timer.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/httpTask")
    public String httpTask(@RequestBody RegisterTaskForHttp registerTask) {
        registerTask(registerTask, registerTask.createTask());
        return "SUCCESSFUL";
    }

    /**
     * 通过bean和method的执行任务
     *
     * @param registerTask
     */
    @PostMapping("/beanTask")
    public String httpTask(@RequestBody RegisterTaskForBean registerTask) {
        registerTask(registerTask, registerTask.createTask());
        return "SUCCESSFUL";
    }

    private void registerTask(RegisterTask registerTask, Runnable task) {
        if (registerTask.isPeriod()) {
            //周期任务
            simpleJob.registerWithFixedDelay(registerTask.getTime(), registerTask.getPeriodT(), task, new StopTaskExceptionHandler());
        } else {
            simpleJob.registerAtTime(registerTask.getTime(), task);
        }
    }
}
