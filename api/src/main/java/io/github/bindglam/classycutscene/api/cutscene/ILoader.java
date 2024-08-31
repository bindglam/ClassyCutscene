package io.github.bindglam.classycutscene.api.cutscene;

import io.github.bindglam.classycutscene.api.JsonParameter;
import io.github.bindglam.classycutscene.api.cutscene.node.AbstractNode;
import io.github.bindglam.classycutscene.api.cutscene.node.INode;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.AbstractKeyframe;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.IKeyframe;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ILoader {
    List<Class<? extends AbstractNode>> getRegisteredNodeTypes();

    List<Class<? extends ICutscene>> getRegisteredCutsceneTypes();

    List<Class<? extends AbstractKeyframe>> getRegisteredKeyframeTypes();

    List<JsonParameter<?>> getRegisteredParameterConverter();

    void registerNode(Class<? extends AbstractNode> clazz);

    void registerCutscene(Class<? extends ICutscene> clazz);

    void registerKeyframe(Class<? extends AbstractKeyframe> clazz);

    void registerParameterConverter(JsonParameter<?> parameter);

    void addPlayingCutscene(ICutscene cutscene);

    void removePlayingCutscene(ICutscene cutscene);

    Map<UUID, ICutscene> getPlayingCutscenes();
}
