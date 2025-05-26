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
        users.set(p.getName()+"guild",guildName);
        return DataManager.reload();
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

    public static Optional<GuildProperty> getGuildProperties(String guildName){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        ConfigurationSection guilds = guildConfig.getConfigurationSection("guilds");
        if (guilds == null) {
            MineCivilization.infoLog("길드 데이터가 없습니다.");
            return Optional.empty();
        }//예외처리
        for (String key : guilds.getKeys(false)) {
            if (key.equals(guildName)) {
                ConfigurationSection guild = guilds.getConfigurationSection(key);
                if (guild == null) continue;
                String rank = guild.getString("rank");
                String leader = guild.getString("leader");
                String viceLeader = guild.getString("vice-leader");
                List<String> members = guild.getStringList("members");
                List<String> claimed = guild.getStringList("claimed");
                List<String> safeZone = guild.getStringList("safe-zone");
                return Optional.of( new GuildProperty(rank, leader, viceLeader, members, claimed, safeZone));
            }
        }
            MineCivilization.infoLog(guildName+" 길드를 찾을 수 없습니다.");
            return Optional.empty();
    }
    public record GuildProperty(String rank, String leader, String viceLeader, List<String> members, List<String> claimed, List<String> safeZone){}

    public static boolean setGuildProperty(String guildName, String target, Object input){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        ConfigurationSection guilds = guildConfig.getConfigurationSection("guilds");
        if (guilds == null){return false;}
        guilds.set(guildName+"."+target,input);
        DataManager.reload();//로직 미완성
        return true;
    }//성공하면 true 반환. 파일 저장,리로드 까지 진행 (되도록 이 함수를 직접 쓰지 말것)

    public static boolean isAvailableGuild(String guildName){
        return getGuildProperties(guildName).isPresent();
    }//존재하는 길드인지 true false 값으로 반환

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
        if(sg && sc && ss) return DataManager.reload(); else return false;
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
        if(sg && sc && ss) return DataManager.reload(); else return false;
    }
    //성공시 청크 데이터 status, claimer, 길드 데이터 claimed가 변경됨
    //성공하면 true, 점령되어있지 않거나 다른 세력이 점령 중이라면 false 반환. // 저장에 실패해도 false 반환.
}
