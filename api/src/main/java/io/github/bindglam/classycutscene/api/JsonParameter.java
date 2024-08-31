package io.github.bindglam.classycutscene.api;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

public interface JsonParameter<T> {
    @Nullable T fromJson(JSONObject obj);
}
