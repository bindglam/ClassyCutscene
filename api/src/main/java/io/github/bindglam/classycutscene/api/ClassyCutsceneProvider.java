package io.github.bindglam.classycutscene.api;

public final class ClassyCutsceneProvider {
    private static ClassyCutscene instance = null;

    public static ClassyCutscene get() {
        return instance;
    }
}
