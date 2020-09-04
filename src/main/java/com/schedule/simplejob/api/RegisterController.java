package com.schedule.simplejob.api;

import com.schedule.simplejob.component.LocalCache;
import com.schedule.simplejob.exchandler.StopTaskExceptionHandler;
import com.schedule.simplejob.model.StatisticalExecModel;
import com.schedule.simplejob.model.TaskPersistModel;
import com.schedule.simplejob.model.UpdateTaskInfo;
import com.schedule.simplejob.model.reqregister.RegisterTask;
import com.schedule.simplejob.model.reqregister.RegisterTaskForBean;
import com.schedule.simplejob.model.reqregister.RegisterTaskForHttp;
import com.schedule.simplejob.timer.SimpleJob;
import com.schedule.simplejob.timer.TimeRunTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    private final static String SUCCESSFUL = "successful";

    private final static String TASK_PERISIT = "TASK_PERISIT";

    /**
     * 通过http调用执行任务
     *
     * @param registerTask
     * @return 任务id
     */
    @PostMapping("/httpTask")
    public String httpTask(@RequestBody RegisterTaskForHttp registerTask) {

        String s = registerTask(registerTask, registerTask.createTask());

        return s;
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

    /**
     * 停止禁用任务的执行
     *
     * @param taskInfo
     * @return
     */
    @PostMapping("/stop")
    public String stopTask(@RequestBody UpdateTaskInfo taskInfo) {

        if (StringUtils.isEmpty(taskInfo.getTaskId())) throw new RuntimeException("the taskId is null");

        simpleJob.stop(taskInfo.getTaskId());

        return SUCCESSFUL;
    }

    /**
     * 查询任务列表
     *
     * @return
     */
    @GetMapping("/list")
    public List<TaskPersistModel> getList() {
        Set<Map.Entry<Object, Object>> list = LocalCache.getInstance().list();
        List<TaskPersistModel> taskPersistModels = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : list) {
            String key = (String) entry.getKey();
            if (key.contains(TASK_PERISIT)) {
                taskPersistModels.add((TaskPersistModel) entry.getValue());
            }
        }

        return taskPersistModels;
    }

    /**
     * 查询任务执行情况
     *
     * @return
     */
    @GetMapping("/statistical")
    public List<StatisticalExecModel> getStatisticalData() {
        LocalCache cache = LocalCache.getInstance();
        Set<Map.Entry<Object, Object>> list = cache.list();
        List<StatisticalExecModel> statisticalExecModels = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : list) {
            String key = (String) entry.getKey();
            if (key.contains(StatisticalExecModel.STATISTICAL_DATA)) {
                StatisticalExecModel statisticalExecModel = (StatisticalExecModel) entry.getValue();
                TaskPersistModel persistModel = (TaskPersistModel) cache.getObject(TASK_PERISIT + statisticalExecModel.getTaskId());
                statisticalExecModel.setName(persistModel.getName());
                statisticalExecModels.add(statisticalExecModel);
            }
        }

        if (!CollectionUtils.isEmpty(statisticalExecModels)) {
            statisticalExecModels.sort(Comparator.comparing(StatisticalExecModel::getExcuteDate).reversed());
        }

        return statisticalExecModels;
    }


    private String registerTask(RegisterTask registerTask, Runnable task) {

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

        if (!isPersistence) {
            //需要进行数据统计
            timeRunTask.setStatistical(true);
            //入本地缓存  假的持久化
            this.addCache(registerTask, timeRunTask.getTaskId());
        }


        return timeRunTask.getTaskId();
    }

    private void addCache(RegisterTask registerTask, String taskId) {

        TaskPersistModel persistModel = TaskPersistModel.builder()
                .id(taskId)
                .args(registerTask.getArgs())
                .cron(registerTask.getCron())
                .desc(registerTask.getDesc())
                .isPeriod(registerTask.isPeriod())
                .periodT(registerTask.getPeriodT())
                .time(registerTask.getTime())
                .name(registerTask.getName())
                .build();

        if (registerTask instanceof RegisterTaskForHttp) {

            RegisterTaskForHttp taskForHttp = (RegisterTaskForHttp) registerTask;

            persistModel.setUrl(taskForHttp.getUrl());
            persistModel.setHttpMethod(taskForHttp.getHttpMethod());

        } else {

            RegisterTaskForBean taskForBean = (RegisterTaskForBean) registerTask;

            persistModel.setBeanName(taskForBean.getBeanName());
            persistModel.setMethodName(taskForBean.getMethodName());
        }


        LocalCache.getInstance().addCache(TASK_PERISIT + taskId, persistModel);

    }
}
