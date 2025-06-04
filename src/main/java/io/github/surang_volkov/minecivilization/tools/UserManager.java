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
        String uuid = getUserUUID(p.getName());
        for(String key : users.getKeys(false)){
            if(uuid.equals(key+".uuid")){
                return false;
            }
        }
        Map<String,String> userData = new HashMap<>();
        userData.put("uuid", uuid);
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
    }//저장된 데이터가 아니라 플레이어 정보에서 직접 uuid를 반환

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
    }//데이터에 저장된 유저인지 boolean 반환

    public static Optional<String> isMember(String username){
        Optional<UserProperty> userProp = getUserProperties(username);
        if(userProp.isEmpty()) return Optional.empty();
        String guildName = userProp.get().guild();
        Optional<GuildManager.GuildProperty> guildProp = GuildManager.getGuildProperties(guildName);
        if(guildProp.isEmpty()) return Optional.empty();
        List<String> memberList = new ArrayList<>(guildProp.get().members());
        if(memberList.contains(username)) return Optional.of(guildName);
        else return Optional.empty();
    }//길드의 멤버인지 여부를 길드 이름으로 반환함. 유저에게 길드가 없으면 none, 유저가 존재하지 않으면 empty 반환.

    public static Optional<Map<String,Object>> isLeader(String username){
        Optional<UserProperty> userProp = getUserProperties(username);
        if(userProp.isEmpty()) return Optional.empty();
        String guildName = userProp.get().guild();
        String userStatus = userProp.get().status();

        Optional<GuildManager.GuildProperty> guildProp = GuildManager.getGuildProperties(guildName);
        if(guildProp.isEmpty()) return Optional.empty();

        if("none".equals(guildName) || "member".equals(userStatus)){
            return Optional.of(Map.of("guildName", guildName, "is", false));
        } else if("leader".equals(userStatus)){
            return Optional.of(Map.of("guildName", guildName, "is", true));
        }
        else return Optional.empty();
    } //길드의 길드장인지 여부를 Map{guildName<String>, is<Boolean>}으로 반환함. 유저에게 길드가 없으면 {none,false}, 유저가 존재하지 않으면 empty 반환.

    public static Optional<Map<String,Object>> isViceLeader(String username){
        Optional<UserProperty> userProp = getUserProperties(username);
        if(userProp.isEmpty()) return Optional.empty();
        String guildName = userProp.get().guild();
        String userStatus = userProp.get().status();

        Optional<GuildManager.GuildProperty> guildProp = GuildManager.getGuildProperties(guildName);
        if(guildProp.isEmpty()) return Optional.empty();

        if("none".equals(guildName) || "member".equals(userStatus)){
            return Optional.of(Map.of("guildName", guildName, "is", false));
        } else if("vice-leader".equals(userStatus)){
            return Optional.of(Map.of("guildName", guildName, "is", true));
        }
        else return Optional.empty();
    } //길드의 부길드장인지 여부를 Map{guildName<String>, is<Boolean>}으로 반환함. 유저에게 길드가 없으면 {none,false}, 유저가 존재하지 않으면 empty 반환.

    public static Optional<UserProperty> getUserProperties(String userName){
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
    //저장된 유저 정보 반환, 유저 정보가 파일에 없으면 empty 반환

    public static boolean setUserProperty(String userName, String target, Object input){
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if(users == null) return false;
        if(isUser(userName)){
            users.set(userName+"."+target,input);
            return DataManager.save();
        }
        return false;
    } //유저 정보 설정

    //그 외 userjoinguild, userleaveguild 같은 건 전부 guild manager 에서 함께 처리하니까 참고
}
