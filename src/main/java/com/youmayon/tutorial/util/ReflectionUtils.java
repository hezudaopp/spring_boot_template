package com.youmayon.tutorial.util;

import org.hibernate.annotations.common.reflection.ReflectionUtil;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * Created by Jawinton on 2017/2/15.
 */
public class ReflectionUtils {

    /**
     * Merge origin's not null attributes to destination
     * Set value by public setter
     * @param origin
     * @param destination
     * @param <T>
     * @return destination
     * @throws IllegalAccessException
     */
    public static final <T> T mergeObject(T origin, T destination) {
        if (origin == null || destination == null) return destination;
        if (!origin.getClass().equals(destination.getClass())) return destination;

        Field[] fields = origin.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String upperFieldName = StringUtils.uppperCaseFirstChar(field.getName());
            String getMethodName = "get" + upperFieldName;
            String setMethodName = "set" + upperFieldName;
            try {
                Method getMethod = origin.getClass().getDeclaredMethod(getMethodName);
                Object destValue = getMethod.invoke(origin);
                if (destValue == null) {
                    continue;
                }
                Method setMethod = origin.getClass().getDeclaredMethod(setMethodName, field.getType());
                setMethod.invoke(destination, destValue);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new InvalidRequestException("Call method " + setMethodName + " exception.");
            }
        }
        return destination;
    }
}
