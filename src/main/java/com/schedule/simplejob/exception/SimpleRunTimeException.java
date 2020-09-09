package com.schedule.simplejob.exception;

/**
 * @Description: <p>运行时基础异常类</p>
 * @Author: zhangbing
 * @CreateDate: 2019/5/29 17:11
 */
public class SimpleRunTimeException extends RuntimeException {
    /**
     * 错误返回时的错误信息
     */
    private final String message;

    /**
     * 错误返回时的错误码
     */
    private final String code;

    private final Object data;



    public SimpleRunTimeException(String code, String message, Object data) {
        super(message);
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public SimpleRunTimeException(String code, String message) {
        super(message);
        this.message = message;
        this.code = code;
        this.data = null;
    }

    public SimpleRunTimeException(String message) {
       this(null,message,null);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Object getData(){return data;}
}
