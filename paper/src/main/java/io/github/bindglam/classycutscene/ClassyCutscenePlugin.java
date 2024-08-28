package io.github.bindglam.classycutscene;

import io.github.bindglam.classycutscene.api.ClassyCutscene;
import io.github.bindglam.classycutscene.api.cutscene.ILoader;
import io.github.bindglam.classycutscene.cutscene.CutsceneLoader;
import io.github.bindglam.classycutscene.utils.ProviderUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class ClassyCutscenePlugin extends JavaPlugin implements ClassyCutscene {
    private final CutsceneLoader loader = new CutsceneLoader();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ProviderUtil.register(this);

        loader.load();
    }

    @Override
    public void onDisable() {
        ProviderUtil.unregister();
    }

    @Override
    public ILoader getLoader() {
        return loader;
    }
}
