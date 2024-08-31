package io.github.bindglam.classycutscene.cutscene.node;

import io.github.bindglam.classycutscene.api.ClassyCutsceneProvider;
import io.github.bindglam.classycutscene.api.cutscene.ICutscene;
import io.github.bindglam.classycutscene.api.cutscene.node.AbstractNode;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CameraNode extends AbstractNode {
    private final ArmorStand cameraEntity;

    private Location location;

    private Location lastLocation;
    private GameMode lastGameMode;

    public CameraNode(String name, Location location) {
        super(name);
        this.location = location;
        cameraEntity = location.getWorld().spawn(location, ArmorStand.class);
        cameraEntity.setGravity(false);
    }

    @Override
    public void tick(ICutscene cutscene) {
        super.tick(cutscene);
        Player player = cutscene.getPlayingPlayer();

        cameraEntity.teleportAsync(location);

        if(lastLocation == null) {
            lastLocation = player.getLocation();
            lastGameMode = player.getGameMode();
        }

        Bukkit.getScheduler().runTask((Plugin) ClassyCutsceneProvider.get(), () -> {
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(cameraEntity);
        });
    }

    @Override
    public void dispose(ICutscene cutscene) {
        super.dispose(cutscene);
        Player player = cutscene.getPlayingPlayer();

        Bukkit.getScheduler().runTaskLater((Plugin) ClassyCutsceneProvider.get(), () -> {
            cameraEntity.remove();

            player.setGameMode(lastGameMode);
            player.teleportAsync(lastLocation);
        }, 10L);
    }
}
