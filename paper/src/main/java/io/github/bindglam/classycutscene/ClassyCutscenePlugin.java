package io.github.bindglam.classycutscene;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.bindglam.classycutscene.api.ClassyCutscene;
import io.github.bindglam.classycutscene.api.cutscene.ICutscene;
import io.github.bindglam.classycutscene.api.cutscene.ILoader;
import io.github.bindglam.classycutscene.cutscene.Cutscene;
import io.github.bindglam.classycutscene.cutscene.CutsceneLoader;
import io.github.bindglam.classycutscene.cutscene.node.CameraNode;
import io.github.bindglam.classycutscene.cutscene.node.EntityNode;
import io.github.bindglam.classycutscene.cutscene.node.keyframe.LinearKeyframe;
import io.github.bindglam.classycutscene.parameters.EntityTypeParameter;
import io.github.bindglam.classycutscene.parameters.LocationParameter;
import io.github.bindglam.classycutscene.utils.ProviderUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ClassyCutscenePlugin extends JavaPlugin implements ClassyCutscene {
    private final CutsceneLoader loader = new CutsceneLoader();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));

        registerCommands();
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        saveDefaultConfig();

        ProviderUtil.register(this);

        loader.registerNode(EntityNode.class);
        loader.registerNode(CameraNode.class);

        loader.registerCutscene(Cutscene.class);

        loader.registerKeyframe(LinearKeyframe.class);

        loader.registerParameterConverter(new LocationParameter());
        loader.registerParameterConverter(new EntityTypeParameter());

        loader.load();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        ProviderUtil.unregister();
    }

    private void registerCommands(){
        new CommandAPICommand("classycutscene")
                .withAliases("ccutscene", "cutscene")
                .withPermission(CommandPermission.OP)
                .withSubcommands(
                        new CommandAPICommand("play")
                                .withArguments(new StringArgument("name"), new PlayerArgument("target").setOptional(true))
                                .executesPlayer((player, args) -> {
                                    String name = (String) args.get("name");
                                    ICutscene cutscene = loader.load(new File("plugins/ClassyCutscene/cutscenes/" + name));

                                    if(cutscene == null){
                                        player.sendMessage(Component.text("Couldn't find cutscene!").color(NamedTextColor.RED));
                                        return;
                                    }

                                    if(args.get("target") != null){
                                        cutscene.play((Player) args.get("target"));
                                    } else {
                                        cutscene.play(player);
                                    }
                                })
                )
                .register();
    }

    @Override
    public ILoader getLoader() {
        return loader;
    }
}
