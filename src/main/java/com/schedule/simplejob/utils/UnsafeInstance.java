package com.schedule.simplejob.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-04-07 15:09
 **/
public class UnsafeInstance {

    public static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
