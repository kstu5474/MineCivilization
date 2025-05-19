package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildManager {
    public static void GenerateGuild(Player p, String name, int index){
        FileConfiguration guildConfig = DataManager.getGuildsConfig();
        Map<String , Map<String,Object>> GuildData = new HashMap<>();
        GuildData.put(name,new GuildProperties.Builder()
                .leader(p.getName())
                .viceLeader("none")
                .safeZone(List.of(Integer.toString(index)))
                .build());
    }
    private static class GuildProperties{
        public String name;
        public String rank;
        public String leader;
        public String viceLeader;
        public List<String> members; // default : {prodamp-1, tradeadvantage-1}
        public List<String> claimed; // default : {iron-1, paper-1}
        public List<String> safeZone;

        public GuildProperties(Builder builder) {
            this.name = builder.name;
            this.rank = builder.rank;
            this.leader = builder.leader;
            this.viceLeader = builder.viceLeader;
            this.members = builder.members;
            this.claimed = builder.claimed;
            this.safeZone = builder.safeZone;
        }
        public static class Builder {
            private String name;
            private String rank;
            private String leader = "unclaimed"; // border, safezone, claimed, unclaimed
            private String viceLeader = "none"; // default : none
            private List<String> members;
            private List<String> claimed;
            private List<String> safeZone;

            public Builder name(String name) {this.name = name;return this;}
            public Builder rank(String rank) {this.rank = rank;return this;}
            public Builder leader(String leader) {this.leader = leader;return this;}
            public Builder viceLeader(String viceLeader) {this.viceLeader = viceLeader;return this;}
            public Builder members(List<String> members) {this.members = members;return this;}
            public Builder claimed(List<String> claimed) {this.claimed = claimed;return this;}
            public Builder safeZone(List<String> safeZone) {this.safeZone = safeZone;return this;}

            public Map<String, Object> build() {
                Map<String, Object> map = new HashMap<>();
                //map.put("name", name);
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
}
