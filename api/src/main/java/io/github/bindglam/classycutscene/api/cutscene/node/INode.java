package io.github.bindglam.classycutscene.api.cutscene.node;

import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.IKeyframe;

import java.util.Map;

public interface INode extends Cloneable {
    Map<Long, IKeyframe> getKeyframes();

    void setKeyframes(Map<Long, IKeyframe> keyframes);

    void addKeyframe(IKeyframe keyframe);
}
