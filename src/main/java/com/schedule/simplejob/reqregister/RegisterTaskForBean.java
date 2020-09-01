package com.schedule.simplejob.reqregister;

import com.schedule.simplejob.config.SpringContextUtil;
import com.schedule.simplejob.utils.SimpleAssert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-01 16:29
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterTaskForBean {

    private String beanName;

    private String methodName;

    private boolean isPeriod;

    private long time;

    private long periodT;

    private String args;

    public Runnable createTask() {

        SimpleAssert.notEmptyString(beanName, "the beanName is null");
        SimpleAssert.notEmptyString(methodName, "the methodName is null");
        if (isPeriod)
            SimpleAssert.notTrue(periodT <= 0, "If it is a periodic task, period cannot be less than 0");


        Object bean = SpringContextUtil.getBean(beanName);

        if (Objects.isNull(bean)) {
            throw new RuntimeException("not found the bean by beanName");
        }

        boolean haveArgs = false;

        Method method = ReflectionUtils.findMethod(bean.getClass(), methodName);

        if (Objects.isNull(method)) {

            method = ReflectionUtils.findMethod(bean.getClass(), methodName, String.class);
            haveArgs = Objects.nonNull(method);
        }

        if (Objects.isNull(method)) {
            throw new RuntimeException("not found the method by methodName");
        }

        Runnable runnable = null;
        if (haveArgs) {
            Method noArgsMethod = method;
            runnable = () -> ReflectionUtils.invokeMethod(noArgsMethod, bean, args);
        } else {
            Method hadArgsMethod = method;
            runnable = () -> ReflectionUtils.invokeMethod(hadArgsMethod, bean);
        }

        return runnable;
    }
}
