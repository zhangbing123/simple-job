package com.schedule.simplejob.reqregister;

import com.schedule.simplejob.utils.RestTemplate;
import com.schedule.simplejob.utils.SimpleAssert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 注册http任务的请求参数
 * @author: zhangbing
 * @create: 2020-09-02 10:55
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterTaskForHttp extends RegisterTask {

    private String url;//地址
    private String httpMethod;//请求方式 目前只支持POST  GET请求
//    private String accept;//指定客户端能够接收的内容类型，内容类型中的先后次序表示客户端接收的先后次序。
    //显示此HTTP请求提交的内容类型。一般只有post提交时才需要设置该属性。 application/x-www-form-urlencoded,application/x-www-form-urlencoded
//    private String contentType;

    public Runnable createTask() {
        SimpleAssert.notEmptyString(url, "the url is null");
        SimpleAssert.notEmptyString(httpMethod, "the httpMethod is null");
        if (isPeriod)
            SimpleAssert.notTrue(periodT <= 0, "If it is a periodic task, period cannot be less than 0");

        return () -> RestTemplate.handleReq(this);
    }

}
