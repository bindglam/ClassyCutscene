package io.github.bindglam.classycutscene.api.cutscene.node;

import io.github.bindglam.classycutscene.api.cutscene.ICutscene;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.IKeyframe;

import java.util.Map;

public interface INode {
    String getName();

    Map<Long, IKeyframe> getKeyframes();

    void setKeyframes(Map<Long, IKeyframe> keyframes);

    void addKeyframe(IKeyframe keyframe);

    void tick(ICutscene cutscene);

    void dispose(ICutscene cutscene);
}
