/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.block.Block
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.item.Item
 *  net.minecraft.util.ResourceLocation
 */
package me.chachoox.lithium.impl.managers.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Map;
import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.property.util.EnumHelper;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

public class ConfigManager
implements Minecraftable {
    public ArrayList<Module> modules = new ArrayList();
    public static String PATH = Lithium.DIRECTORY.getAbsolutePath();
    public static String MODULES = PATH + File.separator + "modules";

    public void loadConfig() {
        for (Module module : this.modules) {
            try {
                this.loadProperties(module);
                Managers.MODULE.get(DisplayTweaks.class).loadFromConfig(module);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.loadPrefix();
        this.loadQuiver();
        this.loadIgnored();
        this.loadChatFilter();
    }

    public void saveConfig() {
        File path = new File(MODULES + File.separator);
        if (!path.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s modules directory.", path.mkdir() ? "Created" : "Failed to create"));
            return;
        }
        for (Module module : this.modules) {
            try {
                this.saveProperties(module);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.savePrefix();
        this.saveQuiver();
        this.saveIgnored();
        this.saveChatFilter();
    }

    public void saveProperties(Module module) throws IOException {
        JsonObject ignored = new JsonObject();
        File directory = new File(MODULES + this.getDirectory(module));
        if (!directory.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s categories directory.", directory.mkdir() ? "Created" : "Failed to create"));
            return;
        }
        String moduleName = MODULES + this.getDirectory(module) + module.getLabel() + ".json";
        Path outputFile = Paths.get(moduleName, new String[0]);
        if (!Files.exists(outputFile, new LinkOption[0])) {
            Files.createFile(outputFile, new FileAttribute[0]);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson((JsonElement)this.writeProperties(module));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile, new OpenOption[0])));
        writer.write(json);
        writer.close();
    }

    public static void setValueFromJson(Module module, Property property, JsonElement element) {
        if (property.getLabel().equals("Enabled")) {
            if (element.getAsBoolean()) {
                module.setEnabled(true);
            }
            return;
        }
        switch (property.getType()) {
            case "Boolean": {
                property.setValue(element.getAsBoolean());
                break;
            }
            case "Double": {
                property.setValue(element.getAsDouble());
                break;
            }
            case "Float": {
                property.setValue(Float.valueOf(element.getAsFloat()));
                break;
            }
            case "Integer": {
                property.setValue(element.getAsInt());
                break;
            }
            case "String": {
                String str = element.getAsString();
                property.setValue(str.replace("_", " "));
                break;
            }
            case "Bind": {
                property.setValue(new Bind.BindConverter().doBackward(element));
                break;
            }
            case "Enum": {
                property.setValue(EnumHelper.fromString((Enum)property.getValue(), element.getAsString()));
                break;
            }
            case "Color": {
                Color color = new Color(element.getAsInt(), true);
                property.setValue(color);
                break;
            }
            case "ItemList": {
                ItemList itemList = (ItemList)property;
                ArrayList itemArray = new ArrayList();
                element.getAsJsonArray().forEach(elem -> {
                    Item item = Item.getByNameOrId((String)elem.getAsString().replace("minecraft:", ""));
                    if (item == null) {
                        return;
                    }
                    itemArray.add(item);
                });
                itemList.setValue(itemArray);
                break;
            }
            case "BlockList": {
                BlockList blockList = (BlockList)property;
                ArrayList blocks = new ArrayList();
                element.getAsJsonArray().forEach(elem -> {
                    Block block = Block.getBlockFromName((String)elem.getAsString().replace("minecraft:", ""));
                    if (block == null) {
                        return;
                    }
                    blocks.add(block);
                });
                blockList.setValue(blocks);
            }
        }
    }

    public void init() {
        this.modules.addAll(Managers.MODULE.getModules());
        this.loadConfig();
        Logger.getLogger().log(Level.INFO, "Config loaded.");
    }

    private void loadProperties(Module module) throws IOException {
        String moduleName = MODULES + this.getDirectory(module) + module.getLabel() + ".json";
        Path modulePath = Paths.get(moduleName, new String[0]);
        if (!Files.exists(modulePath, new LinkOption[0])) {
            return;
        }
        this.loadPath(modulePath, module);
    }

    private void loadPath(Path path, Module module) throws IOException {
        InputStream stream = Files.newInputStream(path, new OpenOption[0]);
        try {
            ConfigManager.loadFile(new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject(), module);
        }
        catch (IllegalStateException e) {
            Logger.getLogger().log(Level.ERROR, "Bad Config File for: " + module.getLabel());
            ConfigManager.loadFile(new JsonObject(), module);
        }
        stream.close();
    }

    private static void loadFile(JsonObject input, Module module) {
        for (Map.Entry entry : input.entrySet()) {
            String propertyLabel = (String)entry.getKey();
            JsonElement element = (JsonElement)entry.getValue();
            for (Property<?> property : module.getProperties()) {
                if (propertyLabel.equals(property.getLabel())) {
                    try {
                        ConfigManager.setValueFromJson(module, property, element);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!propertyLabel.equals("Global " + property.getLabel())) continue;
                ColorProperty colorProperty = (ColorProperty)property;
                colorProperty.setGlobal(element.getAsBoolean());
            }
        }
    }

    public JsonObject writeProperties(Module module) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        for (Property<?> property : module.getProperties()) {
            ArrayList list;
            JsonArray jsonArray;
            if (property instanceof ItemList) {
                jsonArray = new JsonArray();
                ItemList itemList = (ItemList)property;
                list = (ArrayList)itemList.getValue();
                list.forEach(item -> {
                    ResourceLocation resourceLocation = (ResourceLocation)Item.REGISTRY.getNameForObject(item);
                    if (resourceLocation != null) {
                        jsonArray.add(resourceLocation.toString());
                    }
                });
                object.add(itemList.getLabel(), (JsonElement)jsonArray);
                continue;
            }
            if (property instanceof BlockList) {
                jsonArray = new JsonArray();
                BlockList blockList = (BlockList)property;
                list = (ArrayList)blockList.getValue();
                list.forEach(block -> jsonArray.add(((ResourceLocation)Block.REGISTRY.getNameForObject(block)).toString()));
                object.add(blockList.getLabel(), (JsonElement)jsonArray);
                continue;
            }
            if (property instanceof ColorProperty) {
                ColorProperty colorProperty = (ColorProperty)property;
                object.add(property.getLabel(), jp.parse(((Color)colorProperty.getValue()).getRGB() + ""));
                object.add("Global " + property.getLabel(), jp.parse(colorProperty.isGlobal() + ""));
                continue;
            }
            if (property.isStringProperty()) {
                String str = (String)property.getValue();
                property.setValue(str.replace(" ", "_"));
            }
            try {
                object.add(property.getLabel(), jp.parse(property.getValue().toString()));
            }
            catch (Exception exception) {}
        }
        return object;
    }

    public String getDirectory(Module module) {
        String directory = "";
        if (module != null) {
            directory = directory + "/" + module.getCategory().getLabel() + "/";
        }
        return directory;
    }

    public void savePrefix() {
        try {
            File file = new File(PATH, "prefix.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Managers.COMMAND.getPrefix());
            out.write("\r\n");
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPrefix() {
        try {
            String line;
            File file = new File(PATH, "prefix.txt");
            if (!file.exists()) {
                this.savePrefix();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                Managers.COMMAND.setPrefix(line);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.savePrefix();
        }
    }

    public void saveQuiver() {
        try {
            File file = new File(PATH, "quiver_arrows.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String string : Managers.MODULE.get(Quiver.class).getList()) {
                try {
                    out.write(string);
                    out.write("\r\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadQuiver() {
        try {
            String line;
            File file = new File(PATH, "quiver_arrows.txt");
            if (!file.exists()) {
                this.saveQuiver();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Managers.MODULE.get(Quiver.class).getList().clear();
            while ((line = br.readLine()) != null) {
                try {
                    Managers.MODULE.get(Quiver.class).getList().add(line);
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveIgnored() {
        try {
            File file = new File(PATH, "ignored_players.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String string : Managers.CHAT.getIgnoredPlayers()) {
                try {
                    out.write(string);
                    out.write("\r\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadIgnored() {
        try {
            String line;
            File file = new File(PATH, "ignored_players.txt");
            if (!file.exists()) {
                this.saveIgnored();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Managers.CHAT.getIgnoredPlayers().clear();
            while ((line = br.readLine()) != null) {
                try {
                    Managers.CHAT.getIgnoredPlayers().add(line);
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChatFilter() {
        try {
            File file = new File(PATH, "filtered_words.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String string : Managers.CHAT.getIgnoredWords()) {
                try {
                    out.write(string);
                    out.write("\r\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadChatFilter() {
        try {
            String line;
            File file = new File(PATH, "filtered_words.txt");
            if (!file.exists()) {
                this.saveChatFilter();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Managers.CHAT.getIgnoredWords().clear();
            while ((line = br.readLine()) != null) {
                try {
                    Managers.CHAT.getIgnoredWords().add(line);
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonArray getModuleArray() {
        JsonArray modulesArray = new JsonArray();
        for (Module module : Managers.MODULE.getModules()) {
            modulesArray.add((JsonElement)this.getModuleObject(module));
        }
        return modulesArray;
    }

    private JsonObject getModuleObject(Module module) {
        JsonObject attribs = new JsonObject();
        attribs.addProperty("Enabled", Boolean.valueOf(module.isEnabled()));
        attribs.addProperty("Drawn", Boolean.valueOf(!module.isHidden()));
        attribs.addProperty("Keybind", GameSettings.getKeyDisplayString((int)module.getKey()));
        if (!module.getProperties().isEmpty()) {
            for (Property<?> property : module.getProperties()) {
                ArrayList list;
                JsonArray jsonArray;
                if (property.getValue() instanceof Number) {
                    attribs.addProperty(property.getLabel(), (Number)property.getValue());
                    continue;
                }
                if (property.getValue() instanceof Boolean) {
                    attribs.addProperty(property.getLabel(), (Boolean)property.getValue());
                    continue;
                }
                if (property instanceof EnumProperty || property instanceof StringProperty) {
                    attribs.addProperty(property.getLabel(), String.valueOf(property.getValue()));
                    continue;
                }
                if (property instanceof ColorProperty) {
                    JsonArray array = new JsonArray();
                    array.add(Boolean.valueOf(((ColorProperty)property.getValue()).isGlobal()));
                    attribs.add(property.getLabel(), (JsonElement)array);
                    continue;
                }
                if (property.getValue() instanceof BlockList) {
                    jsonArray = new JsonArray();
                    BlockList blockList = (BlockList)property;
                    list = (ArrayList)blockList.getValue();
                    list.forEach(block -> jsonArray.add(((ResourceLocation)Block.REGISTRY.getNameForObject(block)).toString()));
                    attribs.add(property.getLabel(), (JsonElement)jsonArray);
                    continue;
                }
                if (!(property.getValue() instanceof ItemList)) continue;
                jsonArray = new JsonArray();
                ItemList itemList = (ItemList)property;
                list = (ArrayList)itemList.getValue();
                list.forEach(item -> {
                    ResourceLocation resourceLocation = (ResourceLocation)Item.REGISTRY.getNameForObject(item);
                    if (resourceLocation != null) {
                        jsonArray.add(resourceLocation.toString());
                    }
                });
                attribs.add(property.getLabel(), (JsonElement)jsonArray);
            }
        }
        JsonObject moduleObject = new JsonObject();
        moduleObject.add(module.getLabel(), (JsonElement)attribs);
        return moduleObject;
    }
}

