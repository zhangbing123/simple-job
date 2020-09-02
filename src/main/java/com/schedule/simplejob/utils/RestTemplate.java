package com.schedule.simplejob.utils;

import com.alibaba.fastjson.JSON;
import com.schedule.simplejob.reqregister.RegisterTaskForHttp;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Zhangbing on 2020/2/17.
 */
@Log4j2
public class RestTemplate {


    /**
     * http调用
     *
     * @param registerTaskForHttp
     * @return
     */
    public static ResponseEntity handleReq(RegisterTaskForHttp registerTaskForHttp) {

        if (HttpMethod.POST.matches(registerTaskForHttp.getHttpMethod())) {
            //POST请求

            return sendPostHttpReq(registerTaskForHttp);

        } else if (HttpMethod.GET.matches(registerTaskForHttp.getHttpMethod())) {
            //GET请求
            return sendGetHttpReq(registerTaskForHttp);
        } else {
            log.error("{} request mode is not supported at present", registerTaskForHttp.getHttpMethod());
            throw new RuntimeException(registerTaskForHttp.getHttpMethod() + "request mode is not supported at present");
        }

    }

    private static ResponseEntity sendGetHttpReq(RegisterTaskForHttp registerTaskForHttp) {
        // 准备header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(requestHeaders);
        return new RestTemplateBuilder().build()
                .exchange(registerTaskForHttp.getUrl(), HttpMethod.GET, httpEntity, String.class);
    }

    private static ResponseEntity sendPostHttpReq(RegisterTaskForHttp registerTaskForHttp) {
        String args = registerTaskForHttp.getArgs();

        Map map = null;
        if (!StringUtils.isEmpty(args)) {
            try {
                map = JSON.parseObject(args, Map.class);
            } catch (Exception e) {
                throw new RuntimeException("参数结构错误");
            }
        }

        if (Objects.isNull(map)) map = new HashMap();

        // 准备header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(map, requestHeaders);
        return new RestTemplateBuilder().build().exchange(registerTaskForHttp.getUrl(), HttpMethod.POST, httpEntity, String.class);

    }
}
