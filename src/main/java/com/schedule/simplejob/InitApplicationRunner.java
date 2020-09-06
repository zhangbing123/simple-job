package com.schedule.simplejob;

import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.model.reqregister.RegisterTask;
import com.schedule.simplejob.model.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.model.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class InitApplicationRunner implements CommandLineRunner {

    @Autowired
    private JobService jobService;

    @Override
    public void run(String... args) throws Exception {

        /**
         * 系统启动之初 将持久化的任务重新注册到任务执行器中
         */

        Job param = Job.builder().deleted(0).status("RUNNING").isPeriod(1).build();

        //查询启用的周期任务
        List<Job> jobs = jobService.select(param);

        if (!CollectionUtils.isEmpty(jobs)) {

            RegisterTask task = null;

            for (Job job : jobs) {

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
                task.setDesc(job.getDescription());
                task.setName(job.getName());
                task.setPeriodT(job.getPeriodTime());
                task.setPeriod(job.getIsPeriod() == 1);
                task.setTime(job.getTime());

                jobService.registerTaskNotPersist(task);
            }

        }

    }
}
