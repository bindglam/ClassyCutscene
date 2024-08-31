package io.github.bindglam.classycutscene.parameters;

import io.github.bindglam.classycutscene.api.JsonParameter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import java.util.Objects;

public class EntityTypeParameter implements JsonParameter<EntityType> {
    @Override
    public @Nullable EntityType fromJson(JSONObject obj) {
        if(!Objects.equals(obj.get("parameter_type"), "entity_type")) return null;
        return EntityType.valueOf((String) obj.get("type"));
    }
}
