package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserManager {
    public static boolean checkIfNewUser(Player p){
        FileConfiguration userconfig = DataManager.getUserConfig();
        ConfigurationSection users = userconfig.getConfigurationSection("users");
        if(users == null) return false;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(p.getName());
        UUID uuid = offlinePlayer.getUniqueId();
        for(String key : users.getKeys(false)){
            if(Objects.equals(users.getString(key+"uuid"),uuid.toString())){
                return false;
            }
        }
        Map<String,String> userData = new HashMap<>();
        userData.put("uuid",uuid.toString());
        userData.put("guild","none");
        userData.put("status","independent");
        users.set(p.getName(),userData);
        return DataManager.reload();
    }
    public static boolean uuidCheck(String name){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        UUID uuid = offlinePlayer.getUniqueId();
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getUniqueId().equals(uuid)){
                return true;
            }
        }
        return false;
    }
}
