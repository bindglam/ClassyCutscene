package io.github.bindglam.classycutscene.api.cutscene;

import io.github.bindglam.classycutscene.api.cutscene.node.INode;
import org.bukkit.entity.Player;

import java.util.Map;

public interface ICutscene {
    Player getPlayingPlayer();

    Map<String, INode> getNodes();

    void play(Player player);

    void stop();

    long getPosition();

    void tick();
}
