package io.github.bindglam.classycutscene.cutscene;

import io.github.bindglam.classycutscene.api.ClassyCutsceneProvider;
import io.github.bindglam.classycutscene.api.cutscene.ICutscene;
import io.github.bindglam.classycutscene.api.cutscene.node.INode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Cutscene implements ICutscene {
    private final AtomicLong position = new AtomicLong(0L);
    private final AtomicReference<Player> playingPlayer = new AtomicReference<>(null);
    private final AtomicBoolean isPlaying = new AtomicBoolean(false);

    private final HashMap<String, INode> nodes = new HashMap<>();

    private Thread tickThread;

    @Override
    public Player getPlayingPlayer() {
        return playingPlayer.get();
    }

    @Override
    public Map<String, INode> getNodes() {
        return nodes;
    }

    @Override
    public void play(Player player) {
        isPlaying.set(true);

        this.playingPlayer.set(player);

        ClassyCutsceneProvider.get().getLoader().addPlayingCutscene(this);

        tickThread = new Thread(() -> {
            while(true){
                if(!isPlaying.get()) break;
                tick();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        tickThread.start();
    }

    @Override
    public void stop() {
        isPlaying.set(false);

        tickThread.interrupt();

        ClassyCutsceneProvider.get().getLoader().removePlayingCutscene(this);

        Bukkit.getScheduler().runTask((Plugin) ClassyCutsceneProvider.get(), () -> {
            for (INode node : nodes.values()) node.dispose(this);

            this.playingPlayer.set(null);
        });
    }

    @Override
    public long getPosition() {
        return position.get();
    }

    @Override
    public void tick() {
        if(!isPlaying.get()) return;
        position.incrementAndGet();

        for(INode node : nodes.values()){
            node.tick(this);
        }
    }
}
