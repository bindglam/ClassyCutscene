package io.github.bindglam.classycutscene.utils;

import io.github.bindglam.classycutscene.api.ClassyCutscene;
import io.github.bindglam.classycutscene.api.ClassyCutsceneProvider;
import org.jetbrains.annotations.NotNull;

public final class ProviderUtil {
    public static void register(@NotNull ClassyCutscene instance){
        try {
            ReflectionUtil.setField(null, ClassyCutsceneProvider.class.getDeclaredField("instance"), instance);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to register ClassyCutscene instance", e);
        }
    }

    public static void unregister(){
        try {
            ReflectionUtil.setField(null, ClassyCutsceneProvider.class.getDeclaredField("instance"), null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to unregister ClassyCutscene", e);
        }
    }
}
