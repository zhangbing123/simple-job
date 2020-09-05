package com.schedule.simplejob.result;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: api返回数据封装类
 * @author: zhangbing
 * @create: 2019-12-03 14:51
 **/
@Builder
@Data
@NoArgsConstructor
public class Result<T> {
    /**
     * 错误返回时的错误码
     */
    private String code;
    /**
     * 错误返回时的错误信息
     */
    private String message;


    /**
     * status为SC_OK时的JSON字符串
     */
    private T data;

    /**
     * 操作成功
     */
    public static final String DEFAULT_SUCCESS_CODE = "0";
    public static final String DEFAULT_SUCCESS_MSG = "操作成功";
    /**
     * 操作异常
     */
    public static final String DEFAULT_FAIL_CODE = "01";
    public static final String DEFAULT_FAIL_MSG = "服务异常";

    /**
     * 构造函数(成功返回时使用)
     *
     * @param object 支持转换JSON格式的Java对象
     */
    public Result(T object) {

        this.code = DEFAULT_SUCCESS_CODE;
        this.message = "";
        this.data = object;
    }

    /**
     * 构造函数(成功返回描述和数据时使用)
     *
     * @param object 支持转换JSON格式的Java对象
     */
    public Result(T object, String message) {

        this.code = DEFAULT_SUCCESS_CODE;
        this.message = message;
        this.data = object;
    }

    /**
     * 构造函数(失败返回时使用)
     *
     * @param code
     * @param message
     */
    public Result(String code, String message) {

        this.code = code;
        this.message = message;
        this.data = null;
    }

    public Result(String code, String message, T data) {

        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return new Result(null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result(data);
    }

    public static <T> Result<T> ok(T data, String message) {
        return new Result(data, message);
    }

    public static <T> Result<T> failed(String message) {

        return new Result(DEFAULT_FAIL_CODE, message);
    }

    public static <T> Result<T> failed(String code, String message) {

        return new Result(code, message);
    }

    public static <T> Result<T> failed(String code, String message, T data) {

        return new Result(code, message, data);
    }

    public boolean isSuccess() {
            return DEFAULT_SUCCESS_CODE.equals(code);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
