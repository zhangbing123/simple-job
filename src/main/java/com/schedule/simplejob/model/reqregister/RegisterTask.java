package com.schedule.simplejob.model.reqregister;

import com.schedule.simplejob.utils.SimpleAssert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @description: 任务注册参数
 * @author: zhangbing
 * @create: 2020-09-02 13:18
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterTask {

    private String taskId;

    protected boolean isPeriod;

    protected long time;

    protected long periodTime;

    protected String args;

    protected String name;

    protected String desc;

    private String cron;//支持cron表达式

    private boolean isStatistical;


    public Runnable createTask() {
        return null;
    }

    protected void check() {

        if (isPeriod)
            SimpleAssert.notTrue(periodTime <= 0, "If it is a periodic task, period cannot be less than 0");


        SimpleAssert.notTrue(isPeriod && !StringUtils.isEmpty(getCron()), "cron and period cannot take effect at the same time");
    }
}
