package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManager {
    public static boolean isNewUser(Player p){
        FileConfiguration userconfig = DataManager.getUserConfig();
        ConfigurationSection users = userconfig.getConfigurationSection("users");
        if(users == null) return false;
        String uuid = getUserUUID(p.getName());
        for(String key : users.getKeys(false)){
            if(uuid.equals(users.getString(key+".uuid"))){
                return false;
            }
        }
        return true;
    } // 새로 온 유저인지 체크

    public static void addNewUser(Player p){
        FileConfiguration userconfig = DataManager.getUserConfig();
        ConfigurationSection users = userconfig.getConfigurationSection("users");
        if(users != null){
            String uuid = getUserUUID(p.getName());
            Map<String,String> userData = new HashMap<>();
            userData.put("uuid", uuid);
            userData.put("guild","none");
            userData.put("status","independent");
            users.set(p.getName(),userData);
            DataManager.save();
            DataManager.reload();
        }
    } //뉴비유저 추가 메서드

    public static String getUserUUID(String username){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        String uuid = offlinePlayer.getUniqueId().toString();
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getUniqueId().toString().equals(uuid)){
                return uuid;
            }
        }
        return "none";
    }//저장된 데이터가 아니라 플레이어 정보에서 직접 uuid를 반환

    public static boolean isUser(String username){
        if(username.equals("dummy") || username.equals("none") || username.equals("null")) return false;
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if(users == null) return false;
        for(String key : users.getKeys(false)){
            if(!username.equals(key)) continue;
            String storedUuid = users.getString(username+".uuid");
            String targetUuid = getUserUUID(username);
            if(targetUuid.equals(storedUuid)){
                return true;
            }
        }
        return false;
    }//데이터에 저장된 유저인지 boolean 반환

    public static boolean isMember(String username){
        if(!isUser(username)) return false;
        Optional<UserProperty> userProp = getUserProperties(username);
        if(userProp.isEmpty()) return false;
        String userStatus = userProp.get().status();
        return !userStatus.equals("independent");
    }//아무 길드의 일원인지 boolean으로 반환. 유저의 status 확인이므로 independent일때만 false 반환.

    public static boolean isMemberOf(String guildName, String username){
        if(!isUser(username)) return false;
        Optional<UserProperty> userProp = getUserProperties(username);
        if(userProp.isEmpty()) return false;
        String userGuild = userProp.get().guild();
        return userGuild.equals(guildName);
    }//특정 길드의 멤버인지 여부를 boolean으로 반환

    public static boolean isLeader(String username){
        if(!isUser(username)) return false;
        Optional<UserProperty> userProp = getUserProperties(username);
        if(userProp.isEmpty()) return false;
        String userStatus = userProp.get().status();

        return userStatus.equals("leader");
    } //길드의 길드장인지 여부를 boolean 으로 반환

    public static boolean isViceLeader(String username){
        if(!isUser(username)) return false;
        Optional<UserProperty> userProp = getUserProperties(username);
        if(userProp.isEmpty()) return false;
        String userStatus = userProp.get().status();

        return userStatus.equals("vice-leader");
    } //길드의 부길드장인지 여부를 boolean 으로 반환

    public static Optional<UserProperty> getUserProperties(String username){
        if(!isUser(username)) return Optional.empty();;
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if(users == null) return Optional.empty();
        if(isUser(username)){
            String uuid = users.getString(username +".uuid");
            String guild = users.getString((username +".guild"));
            String status = users.getString((username +".status"));
            return Optional.of(new UserProperty(uuid,guild,status));
        }
        return Optional.empty();
    }
    public record UserProperty(String uuid, String guild, String status){}
    //저장된 유저 정보 반환, 유저 정보가 파일에 없으면 empty 반환

    public static boolean setUserProperty(String username, String target, Object input){
        if(!isUser(username)) return false;
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if(users == null) return false;
        if(isUser(username)){
            users.set(username +"."+target,input);
            return DataManager.save();
        }
        return false;
    } //유저 정보 설정

    //그 외 userjoinguild, userleaveguild 같은 건 전부 guild manager 에서 함께 처리하니까 참고
}
