package io.github.bindglam.classycutscene.cutscene.node;

import io.github.bindglam.classycutscene.api.cutscene.ICutscene;
import io.github.bindglam.classycutscene.api.cutscene.node.AbstractNode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityNode extends AbstractNode {
    private final Entity entity;

    private Location location;

    public EntityNode(String name, Location location, EntityType type) {
        super(name);
        this.location = location;
        this.entity = location.getWorld().spawnEntity(location, type);
        //this.entity.setPersistent(true);
        this.entity.setGravity(false);
    }

    @Override
    public void tick(ICutscene cutscene) {
        super.tick(cutscene);

        entity.teleportAsync(location);
    }

    @Override
    public void dispose(ICutscene cutscene) {
        entity.remove();
    }
}
