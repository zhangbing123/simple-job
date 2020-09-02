package com.schedule.simplejob.reqregister;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 任务注册参数
 * @author: zhangbing
 * @create: 2020-09-02 13:18
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterTask {

    protected boolean isPeriod;

    protected long time;

    protected long periodT;

    protected String args;

    public Runnable createTask() {
        return null;
    }
}
