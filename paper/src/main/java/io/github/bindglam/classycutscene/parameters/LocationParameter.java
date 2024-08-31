package io.github.bindglam.classycutscene.parameters;

import io.github.bindglam.classycutscene.api.JsonParameter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import java.util.Objects;

public class LocationParameter implements JsonParameter<Location> {
    @Override
    public @Nullable Location fromJson(JSONObject obj) {
        if(!Objects.equals(obj.get("parameter_type"), "location")) return null;
        return new Location(Bukkit.getWorld((String) obj.get("world")), (Double) obj.get("x"), (Double) obj.get("y"), (Double) obj.get("z"),
                (float) ((double) obj.get("yaw")), (float) ((double) obj.get("pitch")));
    }
}
