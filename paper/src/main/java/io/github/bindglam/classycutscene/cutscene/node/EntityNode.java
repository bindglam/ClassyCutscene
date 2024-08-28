package io.github.bindglam.classycutscene.cutscene.node;

import io.github.bindglam.classycutscene.api.cutscene.node.INode;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.IKeyframe;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityNode implements INode {
    private HashMap<Long, IKeyframe> keyframes = new HashMap<>();

    private final Entity entity;
    private final Location startLocation;

    public EntityNode(EntityType type, Location location) {
        this.startLocation = location;
        this.entity = location.getWorld().spawnEntity(location, type);
        this.entity.setPersistent(true);
    }

    public Entity getEntity() {
        return entity;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    @Override
    public Map<Long, IKeyframe> getKeyframes() {
        return keyframes;
    }

    @Override
    public void setKeyframes(Map<Long, IKeyframe> keyframes) {
        this.keyframes = new HashMap<>(keyframes);
    }

    @Override
    public void addKeyframe(IKeyframe keyframe){
        keyframes.put(keyframe.getPosition(), keyframe);
    }

    @Override
    protected Object clone() {
        EntityNode node = new EntityNode(entity.getType(), startLocation);
        node.setKeyframes(keyframes);
        return node;
    }
}
