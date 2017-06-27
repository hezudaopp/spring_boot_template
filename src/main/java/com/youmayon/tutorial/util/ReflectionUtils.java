package com.youmayon.tutorial.util;

import java.lang.reflect.Field;

/**
 * 反射工具类
 * Created by Jawinton on 2017/2/15.
 */
public class ReflectionUtils {

    /**
     * Merge origin's not null attributes to destination
     * @param origin
     * @param destination
     * @param <T>
     * @return destination
     * @throws IllegalAccessException
     */
    public static final <T> T mergeObject(T origin, T destination) throws IllegalAccessException {
        if (origin == null || destination == null) return destination;
        if (!origin.getClass().equals(destination.getClass())) return destination;

        Field[] fields = origin.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(origin);
            if (null != value) {
                fields[i].set(destination, value);
            }
            fields[i].setAccessible(false);
        }
        return destination;
    }
}
