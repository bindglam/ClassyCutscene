package io.github.bindglam.classycutscene.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ReflectionUtil {
    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(obj, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }
}
