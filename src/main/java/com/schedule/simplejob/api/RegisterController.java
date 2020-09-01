package com.schedule.simplejob.api;

import com.schedule.simplejob.exchandler.StopTaskExceptionHandler;
import com.schedule.simplejob.reqregister.RegisterTaskForBean;
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
    public String httpTask(String url, String requestType, String contentType, String param) {

        return "s";
    }

    /**
     * 通过bean和method的执行任务
     *
     * @param registerTask
     */
    @PostMapping("/beanTask")
    public void httpTask(@RequestBody RegisterTaskForBean registerTask) {

        Runnable task = registerTask.createTask();
        if (registerTask.isPeriod()) {
            simpleJob.registerWithFixedDelay(registerTask.getTime(), registerTask.getPeriodT(), task, new StopTaskExceptionHandler());
        } else {
            simpleJob.registerAtTime(registerTask.getTime(), task);
        }

    }
}
