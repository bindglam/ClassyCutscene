package io.github.bindglam.classycutscene.cutscene.node.keyframe;

import io.github.bindglam.classycutscene.api.cutscene.node.INode;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.AbstractKeyframe;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.IKeyframe;
import io.github.bindglam.classycutscene.utils.ExtraMath;
import io.github.bindglam.classycutscene.utils.ReflectionUtil;
import org.bukkit.Location;
import org.joml.Quaternionf;

import java.lang.reflect.Field;
import java.util.Map;

public final class LinearKeyframe extends AbstractKeyframe {
    public LinearKeyframe(long position, Map<String, Object> targetFields) {
        super(position, targetFields);
    }

    @Override
    public void update(INode node, IKeyframe prevKeyframe, long ticks, long length) {
        float rate = ticks / (float) length;

        for(String fieldName : getTargetFields().keySet()){
            Field field;
            try {
                field = ReflectionUtil.getField(node.getClass(), fieldName);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Failed to edit node data", e);
            }

            if(field.getType() == Location.class){
                Location targetLoc = (Location) getTargetFields().get(fieldName);
                if(prevKeyframe == null){
                    ReflectionUtil.setField(node, field, targetLoc);
                    return;
                }
                Location location = (Location) prevKeyframe.getTargetFields().get(fieldName);

                Quaternionf rotQ = ExtraMath.fromYawPitch(location.getYaw(), location.getPitch(), 0f);
                Quaternionf targetRotQ = ExtraMath.fromYawPitch(targetLoc.getYaw(), targetLoc.getPitch(), 0f);
                rotQ.nlerp(targetRotQ, rate);
                float[] angles = ExtraMath.toEulerAngles(rotQ);

                ReflectionUtil.setField(node, field, new Location(location.getWorld(),
                        ExtraMath.lerp((float) location.getX(), (float) targetLoc.getX(), rate),
                        ExtraMath.lerp((float) location.getY(), (float) targetLoc.getY(), rate),
                        ExtraMath.lerp((float) location.getZ(), (float) targetLoc.getZ(), rate), angles[0], angles[1]));
            }
        }
    }
}
