package io.github.bindglam.classycutscene.cutscene;

import io.github.bindglam.classycutscene.api.JsonParameter;
import io.github.bindglam.classycutscene.api.cutscene.ICutscene;
import io.github.bindglam.classycutscene.api.cutscene.ILoader;
import io.github.bindglam.classycutscene.api.cutscene.node.AbstractNode;
import io.github.bindglam.classycutscene.api.cutscene.node.INode;
import io.github.bindglam.classycutscene.api.cutscene.node.keyframe.AbstractKeyframe;
import io.github.bindglam.classycutscene.utils.ReflectionUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CutsceneLoader implements ILoader {
    private static final File CUTSCENES_FOLDER = new File("plugins/ClassyCutscene/cutscenes");

    private final HashMap<File, JSONObject> preloadedObjMap = new HashMap<>();

    private final List<Class<? extends ICutscene>> cutsceneTypes = new ArrayList<>();
    private final List<Class<? extends AbstractNode>> nodeTypes = new ArrayList<>();
    private final List<Class<? extends AbstractKeyframe>> keyframeTypes = new ArrayList<>();
    private final List<JsonParameter<?>> parameterConverters = new ArrayList<>();

    public final ConcurrentHashMap<UUID, ICutscene> playingCutscenes = new ConcurrentHashMap<>();
    
    public void load(){
        if(!CUTSCENES_FOLDER.exists())
            CUTSCENES_FOLDER.mkdirs();

        for(File animFile : Objects.requireNonNull(CUTSCENES_FOLDER.listFiles())){
            if(!animFile.getName().endsWith(".json")) continue;
            JSONObject jsonObj;
            try {
                jsonObj = (JSONObject) new JSONParser().parse(new FileReader(animFile));
            } catch (IOException | ParseException e) {
                throw new RuntimeException("Failed to load cutscene file", e);
            }

            preloadedObjMap.put(animFile, jsonObj);
        }
    }

    public @Nullable ICutscene load(File animFile){
        if(!animFile.exists()) return null;
        JSONObject jsonObj = preloadedObjMap.get(animFile);
        Class<? extends ICutscene> cutsceneClass;
        try {
            cutsceneClass = (Class<? extends ICutscene>) Class.forName((String) jsonObj.get("class"));
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException("Failed to load cutscene type", e);
        }
        ICutscene cutscene;
        try {
            cutscene = cutsceneClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to load cutscene type", e);
        }

        HashMap<String, INode> nodes = new HashMap<>();
        for(JSONObject nodeObj : (List<JSONObject>) jsonObj.get("nodes")) {
            String name = (String) nodeObj.get("name");
            Class<? extends AbstractNode> nodeClass;
            try {
                nodeClass = (Class<? extends AbstractNode>) Class.forName((String) nodeObj.get("class"));
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new RuntimeException("Failed to load node type", e);
            }
            if(!nodeTypes.contains(nodeClass))
                throw new RuntimeException("Couldn't find node type");

            List<JSONObject> parameterObjList = (List<JSONObject>) nodeObj.get("parameters");
            AbstractNode node;
            try {
                Constructor<?> constructor = nodeClass.getDeclaredConstructors()[0];
                List<Object> parameters = new ArrayList<>(List.of(name));

                for(int i = 0; i < constructor.getParameterTypes().length-1; i++){
                    JSONObject parameterObj = parameterObjList.get(i);
                    parameters.add(convertJson(parameterObj));
                }

                node = (AbstractNode) constructor.newInstance(parameters.toArray(new Object[0]));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to load node", e);
            }

            for(JSONObject keyframeObj : (List<JSONObject>) nodeObj.get("keyframes")){
                Class<? extends AbstractKeyframe> keyframeClass;
                try {
                    keyframeClass = (Class<? extends AbstractKeyframe>) Class.forName((String) keyframeObj.get("class"));
                } catch (ClassNotFoundException | ClassCastException e) {
                    throw new RuntimeException("Failed to load keyframe type", e);
                }
                if(!keyframeTypes.contains(keyframeClass))
                    throw new RuntimeException("Couldn't find keyframe type");

                HashMap<String, Object> targetFields = new HashMap<>();
                for(JSONObject fieldObj : (List<JSONObject>)  keyframeObj.get("fields")){
                    Class<?> fieldClass;
                    try {
                        fieldClass = ReflectionUtil.getField(node.getClass(), (String) fieldObj.get("name")).getType();
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException("Failed to load keyframe value", e);
                    }

                    if(fieldClass == Location.class){
                        targetFields.put((String) fieldObj.get("name"), convertJson((JSONObject) fieldObj.get("value")));
                    }
                }

                AbstractKeyframe keyframe;
                try {
                    keyframe = keyframeClass.getDeclaredConstructor(long.class, Map.class).newInstance(keyframeObj.get("position"), targetFields);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException("Failed to create keyframe", e);
                }
                node.addKeyframe(keyframe);
            }

            nodes.put(name, node);
        }

        cutscene.getNodes().putAll(nodes);
        return cutscene;
    }

    @Override
    public List<Class<? extends AbstractNode>> getRegisteredNodeTypes() {
        return new ArrayList<>(nodeTypes);
    }

    @Override
    public List<Class<? extends ICutscene>> getRegisteredCutsceneTypes() {
        return new ArrayList<>(cutsceneTypes);
    }

    @Override
    public List<Class<? extends AbstractKeyframe>> getRegisteredKeyframeTypes() {
        return new ArrayList<>(keyframeTypes);
    }

    @Override
    public List<JsonParameter<?>> getRegisteredParameterConverter() {
        return new ArrayList<>(parameterConverters);
    }

    @Override
    public void registerNode(Class<? extends AbstractNode> clazz) {
        nodeTypes.add(clazz);
    }

    @Override
    public void registerCutscene(Class<? extends ICutscene> clazz) {
        cutsceneTypes.add(clazz);
    }

    @Override
    public void registerKeyframe(Class<? extends AbstractKeyframe> clazz) {
        keyframeTypes.add(clazz);
    }

    @Override
    public void registerParameterConverter(JsonParameter<?> parameter){
        parameterConverters.add(parameter);
    }

    @Override
    public void addPlayingCutscene(ICutscene cutscene) {
        playingCutscenes.put(cutscene.getPlayingPlayer().getUniqueId(), cutscene);
    }

    @Override
    public void removePlayingCutscene(ICutscene cutscene) {
        playingCutscenes.remove(cutscene.getPlayingPlayer().getUniqueId());
    }

    @Override
    public Map<UUID, ICutscene> getPlayingCutscenes() {
        return playingCutscenes;
    }

    public @Nullable Object convertJson(JSONObject obj){
        for(JsonParameter<?> parameter : parameterConverters){
            Object result = parameter.fromJson(obj);
            if(result == null) continue;
            return result;
        }
        return null;
    }
}
