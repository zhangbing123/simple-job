package com.schedule.simplejob.utils;

import com.schedule.simplejob.exception.SimpleRunTimeException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @description: 部分异常需要抛出RossRunTimeException类型的
 * @author: zhangbing
 * @create: 2019-10-15 14:19
 **/
public class SimpleAssert extends Assert {

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new SimpleRunTimeException(message);
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        if (Objects.nonNull(object)) {
            throw new SimpleRunTimeException(message);
        }
    }

    public static void notEmptyString(@Nullable String object, String message) {
        if (StringUtils.isEmpty(object)) {
            throw new SimpleRunTimeException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new SimpleRunTimeException(message);
        }
    }

    public static void notTrue(boolean expression, String message) {
        if (expression) {
            throw new SimpleRunTimeException(message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new SimpleRunTimeException(message);
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new SimpleRunTimeException(message);
        }
    }

    public static void strLimitLength(String string, int len, String message) {
        if (!StringUtils.isEmpty(string) && string.length() > len) {
            throw new SimpleRunTimeException(message);
        }
    }

}
