package org.feidian.dha.spring.boot.autoconfigure.config;

import java.lang.reflect.Field;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-21 19:23
 **/

public class ReflectUtil {
    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
            }
        }
        return field;
    }

    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param obj        目标对象
     * @param fieldName  目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName,
        String fieldValue) {
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
