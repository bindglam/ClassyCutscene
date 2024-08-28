package io.github.bindglam.classycutscene.cutscene.node.keyframe;

import io.github.bindglam.classycutscene.api.cutscene.node.INode;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.IKeyframe;
import io.github.bindglam.classycutscene.utils.ReflectionUtil;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.util.Map;

public final class LinearKeyframe implements IKeyframe {
    private final Map<String, Object> targetFields;
    private final long position;

    public LinearKeyframe(long position, Map<String, Object> targetFields) {
        this.targetFields = targetFields;
        this.position = position;
    }

    @Override
    public Map<String, Object> getTargetFields() {
        return targetFields;
    }

    @Override
    public void update(INode node, long ticks, long length) {
        float rate = ticks / (float) length;

        for(String fieldName : targetFields.keySet()){
            Field field;
            Object value;
            try {
                field = node.getClass().getDeclaredField(fieldName);
                value = field.get(node);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to edit node data", e);
            }

            if(field.getType() == Location.class){
                Location location = (Location) value;
                Location targetLoc = (Location) targetFields.get(fieldName);
                double editX = (targetLoc.getX()-location.getX())*rate;
                double editY = (targetLoc.getY()-location.getY())*rate;
                double editZ = (targetLoc.getZ()-location.getZ())*rate;

                ReflectionUtil.setField(node, field, new Location(location.getWorld(),
                        location.getX()+editX, location.getY()+editY, location.getZ()+editZ));
            }
        }
    }

    @Override
    public long getPosition() {
        return position;
    }
}
