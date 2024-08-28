package io.github.bindglam.classycutscene.api.cutscene.node.keyframe;

import io.github.bindglam.classycutscene.api.cutscene.node.INode;

import java.util.Map;

public interface IKeyframe {
    Map<String, Object> getTargetFields();

    void update(INode node, long ticks, long length);

    long getPosition();
}
