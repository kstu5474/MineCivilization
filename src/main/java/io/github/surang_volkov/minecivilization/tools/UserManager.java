package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManager {
    public static boolean checkIfNewUser(Player p){
        FileConfiguration userconfig = DataManager.getUserConfig();
        ConfigurationSection users = userconfig.getConfigurationSection("users");
        if(users == null) return false;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(p.getName());
        UUID uuid = offlinePlayer.getUniqueId();
        for(String key : users.getKeys(false)){
            if(Objects.equals(users.getString(key+".uuid"),uuid.toString())){
                return false;
            }
        }
        Map<String,String> userData = new HashMap<>();
        userData.put("uuid",uuid.toString());
        userData.put("guild","none");
        userData.put("status","independent");
        users.set(p.getName(),userData);
        DataManager.save();
        return DataManager.reload();
    } // 새로 온 유저인지 체크하고 그렇다면 유저 리스트에 정보를 추가함

    public static String getUserUUID(String name){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        String uuid = offlinePlayer.getUniqueId().toString();
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getUniqueId().toString().equals(uuid)){
                return uuid;
            }
        }
        return "none";
    }//저장된 데이터가 아닌 이름으로 uuid를 반환

    public static boolean isUser(String username){
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if(users == null) return false;
        for(String key : users.getKeys(false)){
            if(Objects.equals(key, username)){
                return true;
            }
        }
        return false;
    }

    public static Optional<UserProperty> getUserProperty(String userName){
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if(users == null) return Optional.empty();
        if(isUser(userName)){
            String uuid = users.getString(userName+".uuid");
            String guild = users.getString((userName+".guild"));
            String status = users.getString((userName+".status"));
            return Optional.of(new UserProperty(uuid,guild,status));
        }
        return Optional.empty();
    }
    public record UserProperty(String uuid, String guild, String status){}

    public static boolean setUserProperty(String userName, String target, Object input){
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if(users == null) return false;
        if(isUser(userName)){
            users.set(userName+"."+target,input);
            return DataManager.save();
        }
        return false;
    }
    //userjoinguild, userleaveguild
}
