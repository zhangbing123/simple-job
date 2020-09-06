package com.schedule.simplejob.model.reqregister;

import com.schedule.simplejob.config.SpringApplicationContextUtil;
import com.schedule.simplejob.utils.SimpleAssert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Builder
@Data
public class RegisterTaskForBean extends RegisterTask {

    private String beanName;

    private String methodName;


    @Override
    public Runnable createTask() {

        SimpleAssert.notEmptyString(beanName, "the beanName is null");
        SimpleAssert.notEmptyString(methodName, "the methodName is null");

        super.check();

        Object bean = SpringApplicationContextUtil.getBean(beanName);

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
