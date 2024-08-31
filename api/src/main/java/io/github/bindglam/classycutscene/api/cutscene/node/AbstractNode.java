package io.github.bindglam.classycutscene.api.cutscene.node;

import io.github.bindglam.classycutscene.api.ClassyCutsceneProvider;
import io.github.bindglam.classycutscene.api.cutscene.ICutscene;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.IKeyframe;
import org.joml.Quaternionf;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractNode implements INode {
    private final String name;

    private ConcurrentHashMap<Long, IKeyframe> keyframes = new ConcurrentHashMap<>();

    private int keyframeIndex = 0;

    public AbstractNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<Long, IKeyframe> getKeyframes() {
        return keyframes;
    }

    @Override
    public void setKeyframes(Map<Long, IKeyframe> keyframes) {
        this.keyframes = new ConcurrentHashMap<>(keyframes);
    }

    @Override
    public void addKeyframe(IKeyframe keyframe){
        keyframes.put(keyframe.getPosition(), keyframe);
    }

    @Override
    public void tick(ICutscene cutscene) {
        if(cutscene.getPlayingPlayer() == null) return;
        IKeyframe keyframe;
        try {
            keyframe = getKeyframes().values().stream().toList().get(keyframeIndex);

            if(keyframe.getPosition() < cutscene.getPosition()){
                keyframeIndex++;
                keyframe = getKeyframes().values().stream().toList().get(keyframeIndex);
            }
        } catch (ArrayIndexOutOfBoundsException e){
            cutscene.stop();
            return;
        }

        if(keyframeIndex == 0) {
            keyframe.update(this, null, cutscene.getPosition(), keyframe.getPosition());
        } else {
            IKeyframe prevKeyframe = getKeyframes().values().stream().toList().get(keyframeIndex-1);
            keyframe.update(this, prevKeyframe, cutscene.getPosition()-prevKeyframe.getPosition(), keyframe.getPosition()-prevKeyframe.getPosition());
        }
    }

    @Override
    public void dispose(ICutscene cutscene) {
    }
}
