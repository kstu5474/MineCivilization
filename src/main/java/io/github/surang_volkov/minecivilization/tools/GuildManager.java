package io.github.surang_volkov.minecivilization.tools;

import io.github.surang_volkov.minecivilization.MineCivilization;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildManager {
    public static boolean CreateGuild(Player p, String guildName){
        int index = ChunkManager.getChunkIndex(p.getChunk().getX(),p.getChunk().getZ());
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        ConfigurationSection guilds = guildConfig.getConfigurationSection("guilds");
        FileConfiguration userConfig = DataManager.getUserConfig();
        ConfigurationSection users = userConfig.getConfigurationSection("users");
        if (guilds == null) return false;
        guilds.set(guildName,new GuildProperties.Builder()
                .rank("minor")
                .leader(p.getName())
                .viceLeader("none")
                .members(List.of(p.getName()))
                .claimed(List.of(Integer.toString(index)))
                .safeZone(List.of(Integer.toString(index)))
                .build());
        if (users == null) return false;
        boolean a = UserManager.setUserProperty(p.getName(),"guild",guildName);
        boolean b = UserManager.setUserProperty(p.getName(),"status","leader");
        boolean c = ChunkManager.setChunkProperty(index,"claimer",guildName);
        boolean d = ChunkManager.setChunkProperty(index,"status","claimed");
        if(a&&b&&c&&d){
            DataManager.save();
            return DataManager.reload();
        } else return false;
    }//길드 만들땐 서있는 위치에 처음 영토가 생김. 어떤식으로든 생성에 실패하면 false 반환
    private static class GuildProperties{
        public String rank;
        public String leader;
        public String viceLeader;
        public List<String> members; // default : {prodamp-1, tradeadvantage-1}
        public List<String> claimed; // default : {iron-1, paper-1}
        public List<String> safeZone;

        public GuildProperties(Builder builder) {
            this.rank = builder.rank;
            this.leader = builder.leader;
            this.viceLeader = builder.viceLeader;
            this.members = builder.members;
            this.claimed = builder.claimed;
            this.safeZone = builder.safeZone;
        }
        public static class Builder {
            private String rank;
            private String leader; // border, safezone, claimed, unclaimed
            private String viceLeader = "none"; // default : none
            private List<String> members;
            private List<String> claimed;
            private List<String> safeZone;

            public Builder rank(String rank) {this.rank = rank;return this;}
            public Builder leader(String leader) {this.leader = leader;return this;}
            public Builder viceLeader(String viceLeader) {this.viceLeader = viceLeader;return this;}
            public Builder members(List<String> members) {this.members = members;return this;}
            public Builder claimed(List<String> claimed) {this.claimed = claimed;return this;}
            public Builder safeZone(List<String> safeZone) {this.safeZone = safeZone;return this;}

            public Map<String, Object> build() {
                Map<String, Object> map = new HashMap<>();
                map.put("rank", rank);
                map.put("leader", leader);
                map.put("vice-leader", viceLeader);
                map.put("members", members);
                map.put("claimed", claimed);
                map.put("safe-zone", safeZone);
                return map;
            }
        }
    }

    public static boolean removeGuild(Player p, String guildName){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        ConfigurationSection guilds = guildConfig.getConfigurationSection("guilds");
        if(guilds == null) return false;
        String senderUUID = UserManager.getUserUUID(p.getName());
        String targetUUID = UserManager.getUserUUID(guilds.getString(guildName+".leader"));
        if(Objects.equals(senderUUID,targetUUID)){
            Optional<GuildProperty> guildProp = getGuildProperties(guildName);
            if(guildProp.isEmpty()) return false;
            for(String key : guildProp.get().claimed){
                int index = Integer.parseInt(key);
                removeGuildTerritory(guildName, index);
            }
            boolean a = UserManager.setUserProperty(p.getName(),"guild","none");
            boolean b = UserManager.setUserProperty(p.getName(),"status","independent");
            boolean c = removeGuildMethod("guilds."+guildName);
            if(a&&b&&c) {
                MineCivilization.infoLog("길드가 삭제되었습니다.");
                return DataManager.save(); // 모든 작업 후 실행
            }
        }
        MineCivilization.errorLog("허가되지 않은 유저가 길드삭제를 시도했습니다.");
        return false;
    }
    public static boolean removeGuildMethod(String target){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        guildConfig.set(target,null);
        return true;
    }
    // 중요: 길드를 완전히 삭제해버리니까 길마만 삭제할 수 있도록 입력하는 유저 확인을 제대로 할 것.
    // 제대로 완성 못했다 멤버 삭제를 못함 아직

    public static boolean isGuild(String guildName){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        ConfigurationSection guilds = guildConfig.getConfigurationSection("guilds");
        if (guilds == null) return false;
        for (String key : guilds.getKeys(false)) {
            if (key.equals(guildName)) return true;
        }
        return false;
    }

    public static Optional<GuildProperty> getGuildProperties(String guildName){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        ConfigurationSection guilds = guildConfig.getConfigurationSection("guilds");
        if (guilds == null) {
            MineCivilization.infoLog("길드 데이터가 없습니다.");
            return Optional.empty();
        }//예외처리
        if(isGuild(guildName)){
            ConfigurationSection guild = guilds.getConfigurationSection(guildName);
            if (guild == null) return Optional.empty();
            String rank = guild.getString("rank");
            String leader = guild.getString("leader");
            String viceLeader = guild.getString("vice-leader");
            List<String> members = guild.getStringList("members");
            List<String> claimed = guild.getStringList("claimed");
            List<String> safeZone = guild.getStringList("safe-zone");
            return Optional.of( new GuildProperty(rank, leader, viceLeader, members, claimed, safeZone));
        }
            MineCivilization.infoLog(guildName+" 길드를 찾을 수 없습니다.");
            return Optional.empty();
    }// 길드 정보 및 속성을 불러옴. 존재하지 않는 길드는 empty() 반환. isEmpty()로 체크 가능
    public record GuildProperty(String rank, String leader, String viceLeader, List<String> members, List<String> claimed, List<String> safeZone){}

    public static boolean setGuildProperty(String guildName, String target, Object input){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        ConfigurationSection guilds = guildConfig.getConfigurationSection("guilds");
        if (guilds == null){return false;}
        guilds.set(guildName+"."+target,input);
        DataManager.save();//로직 미완성
        return true;
    }//성공하면 true 반환. 파일 저장,리로드 까지 진행 (되도록 이 함수를 직접 쓰지 말것)

    public static boolean addGuildMember(String guildName,String newComer){
        if(getGuildProperties(guildName).isEmpty())return false;
        List<String> memberList = new ArrayList<>(getGuildProperties(guildName).get().members);
        if (memberList.contains(newComer)){
            return false;
        }else{
            memberList.add(newComer);
            return setGuildProperty(guildName, "members", memberList);
        }
    }//길드멤버 추가. 길드에 이미 유저가 있다면 false 반환. 서버에 존재하는 유저인지 추가 확인 필요

    public static boolean removeGuildMember(String guildName,String member){
        if(getGuildProperties(guildName).isEmpty())return false;
        List<String> memberList = new ArrayList<>(getGuildProperties(guildName).get().members);
        if (!memberList.contains(member)){
            return false;
        }else{
            memberList.remove(member);
            return setGuildProperty(guildName,"members", memberList);
        }
    }//길드멤버 제거. 길드에 해당 유저가 없다면 false 반환.

    public static boolean addGuildTerritory(String guildName, int index){
        Optional<ChunkManager.ChunkProperty> chunkOpt = ChunkManager.getChunkProperty(index);
        if (chunkOpt.isEmpty()) return false;
        ChunkManager.ChunkProperty chunkProp = chunkOpt.get();

        Optional<GuildProperty> guildOpt = getGuildProperties(guildName);
        if (guildOpt.isEmpty()) return false;
        GuildProperty guildProp = guildOpt.get();

        if(Objects.equals(chunkProp.status(),"claimed"))return false;
        List<String> territories = new ArrayList<>(guildProp.claimed);
        territories.add(Integer.toString(index));

        boolean sg = setGuildProperty(guildName,"claimed",territories);
        boolean sc = ChunkManager.setChunkProperty(index,"claimer",guildName);
        boolean ss = ChunkManager.setChunkProperty(index,"status","claimed");
        if(sg && sc && ss) return DataManager.save(); else return false;
    }
    //성공시 청크 데이터 status, claimer, 길드 데이터 claimed가 변경됨
    //성공하면 true, 청크가 존재하지 않거나 이미 어떤 세력이 점령 중이라면 false 반환. // 저장에 실패해도 false 반환.

    public static boolean removeGuildTerritory(String guildName, int index){
        Optional<ChunkManager.ChunkProperty> chunkOpt = ChunkManager.getChunkProperty(index);
        if (chunkOpt.isEmpty()) return false;
        ChunkManager.ChunkProperty chunkProp = chunkOpt.get();

        Optional<GuildProperty> guildOpt = getGuildProperties(guildName);
        if (guildOpt.isEmpty()) return false;
        GuildProperty guildProp = guildOpt.get();

        if(Objects.equals(chunkProp.status(),"unclaimed"))return false;
        if (!Objects.equals(chunkProp.claimer(),guildName))return false;
        List<String> territories = new ArrayList<>(guildProp.claimed);
        territories.remove(Integer.toString(index));

        boolean sg = setGuildProperty(guildName,"claimed",territories);
        boolean sc = ChunkManager.setChunkProperty(index,"claimer","none");
        boolean ss = ChunkManager.setChunkProperty(index,"status","unclaimed");
        if(sg && sc && ss) return DataManager.save(); else return false;
    }
    //성공시 청크 데이터 status, claimer, 길드 데이터 claimed가 변경됨
    //성공하면 true, 점령되어있지 않거나 다른 세력이 점령 중이라면 false 반환. // 저장에 실패해도 false 반환.
}
