package io.github.bindglam.classycutscene.cutscene;

import io.github.bindglam.classycutscene.api.cutscene.ILoader;

import java.io.File;

public class CutsceneLoader implements ILoader {
    private static final File CUTSCENES_FOLDER = new File("plugins/ClassyCutscene/cutscenes");
    
    public void load(){
        if(!CUTSCENES_FOLDER.exists())
            CUTSCENES_FOLDER.mkdirs();
    }
}
