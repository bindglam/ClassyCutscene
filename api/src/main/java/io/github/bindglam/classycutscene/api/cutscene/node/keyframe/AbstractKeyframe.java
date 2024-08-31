package io.github.bindglam.classycutscene.api.cutscene.node.keyframe;

import java.util.Map;

public abstract class AbstractKeyframe implements IKeyframe {
    private final Map<String, Object> targetFields;
    private final long position;

    public AbstractKeyframe(long position, Map<String, Object> targetFields) {
        this.targetFields = targetFields;
        this.position = position;
    }

    @Override
    public Map<String, Object> getTargetFields() {
        return targetFields;
    }

    @Override
    public long getPosition() {
        return position;
    }
}
