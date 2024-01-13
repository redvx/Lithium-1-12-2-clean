/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  com.google.gson.GsonBuilder
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.managers.client;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.chachoox.lithium.api.util.friend.Friend;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.Level;

public class FriendManager {
    private File directory;
    private List<Friend> friends = new ArrayList<Friend>();

    public void init() {
        if (!this.directory.exists()) {
            try {
                this.directory.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.loadFriends();
        GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
        if (!this.isFriend(profile.getName())) {
            Managers.FRIEND.addFriend(profile.getName());
            Logger.getLogger().log(Level.INFO, String.format("(SelfFriend) Adding player [%s]", profile.getName()));
        }
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void saveFriends() {
        if (this.directory.exists()) {
            try (FileWriter writer = new FileWriter(this.directory);){
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(this.friends));
            }
            catch (IOException e) {
                this.directory.delete();
            }
        }
    }

    public void loadFriends() {
        if (!this.directory.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(this.directory);){
            this.friends = new ArrayList<Friend>((Collection)new GsonBuilder().setPrettyPrinting().create().fromJson((Reader)inFile, new TypeToken<ArrayList<Friend>>(){}.getType()));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void addFriend(String name) {
        if (this.isFriend(name)) {
            return;
        }
        this.friends.add(new Friend(name));
    }

    public Friend getFriend(String label) {
        for (Friend friend : this.friends) {
            if (!friend.getLabel().equalsIgnoreCase(label)) continue;
            return friend;
        }
        return null;
    }

    public boolean isFriend(String label) {
        return this.getFriend(label) != null;
    }

    public boolean isFriend(EntityPlayer player) {
        return this.getFriend(player.getName()) != null;
    }

    public void removeFriend(String name) {
        Friend friend = this.getFriend(name);
        if (friend != null) {
            this.friends.remove(friend);
        }
    }

    public List<Friend> getFriends() {
        return this.friends;
    }
}

