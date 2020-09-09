package com.schedule.simplejob.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.schedule.simplejob.result.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @description: 全局异常处理类
 * @author: zhangbing
 * @create: 2019-12-03 14:50
 **/
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandle {

    /**
     * 全局异常处理
     *
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public <T> Result<T> handle(Exception e) {
        log.warn("异常信息:{}", e);
        //针对自定义运行异常做特殊处理
        if (e instanceof SimpleRunTimeException) {
            SimpleRunTimeException exception = (SimpleRunTimeException) e;
            return (Result<T>) Result.failed(StringUtils.isEmpty(exception.getCode()) ? Result.DEFAULT_FAIL_CODE : exception.getCode(), exception.getMessage(), exception.getData());
        }
        //所有非自定义异常
        Object o = JSON.toJSON(e);
        if (o == null) {
            return (Result<T>) Result.builder().code(Result.DEFAULT_FAIL_CODE).message(e.getMessage()).build();
        } else {
            List list = (List) ((JSONObject) o).get("stackTrace");
            JSONObject stackTrace = null;
            if (!list.isEmpty()) {
                stackTrace = (JSONObject) list.get(0);
                stackTrace.put("error", e.getMessage() != null ? e.getMessage() : e.toString());
            }
            return (Result<T>) Result.builder().code(Result.DEFAULT_FAIL_CODE).message(Result.DEFAULT_FAIL_MSG).data(stackTrace).build();
        }
    }
}
