package io.github.bindglam.classycutscene.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ReflectionUtil {
    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if(clazz.getSuperclass() == Object.class) throw e;
            return getField(clazz.getSuperclass(), name);
        }
    }
}
