package io.github.surang_volkov.minecivilization.tools;

import io.github.surang_volkov.minecivilization.MineCivilization;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

import static io.github.surang_volkov.minecivilization.tools.DataManager.getChunkConfig;

public class ChunkManager {
    public static void generateChunkProperties(Map<String,Integer> coordinate, int limit){
        int number = 1;
        int r = 0;
        FileConfiguration chunkConfig = getChunkConfig();
        Map<String,Integer> coord = new HashMap<>(coordinate);
        Map<String , Map<String,Object>> chunkDataGenerated = new HashMap<>();
        chunkDataGenerated.put(String.valueOf(number), new ChunkProperties.Builder().level(1).claimer("none").status("unclaimed")
                .coordinate(new HashMap<>(coord)).boost(getRandomBoost()).product(getRandomProduct()).build());
        while (r+1 < limit) {
            r += 1;
            for (int i = 0; i < r; i++){
                coord.put("x", coord.get("x") + 1);
                number += 1;
                chunkDataGenerated.put(String.valueOf(number), new ChunkProperties.Builder()
                        .coordinate(new HashMap<>(coord)).boost(getRandomBoost()).product(getRandomProduct()).build());
            }
            for (int i = 0; i < r; i++){
                coord.put("z", coord.get("z") + 1);
                number += 1;
                chunkDataGenerated.put(String.valueOf(number), new ChunkProperties.Builder()
                        .coordinate(new HashMap<>(coord)).boost(getRandomBoost()).product(getRandomProduct()).build());
            }
            r += 1;
            for (int i = 0; i < r; i++){
                coord.put("x", coord.get("x") - 1);
                number += 1;
                chunkDataGenerated.put(String.valueOf(number), new ChunkProperties.Builder()
                        .coordinate(new HashMap<>(coord)).boost(getRandomBoost()).product(getRandomProduct()).build());
            }
            for (int i = 0; i < r; i++){
                coord.put("z", coord.get("z") - 1);
                number += 1;
                chunkDataGenerated.put(String.valueOf(number), new ChunkProperties.Builder()
                        .coordinate(new HashMap<>(coord)).boost(getRandomBoost()).product(getRandomProduct()).build());
            }
        } // 인덱스 넘버링 로직
        for (int i = 0; i < r; i++){
            coord.put("x", coord.get("x") + 1);
            number += 1;
            chunkDataGenerated.put(String.valueOf(number), new ChunkProperties.Builder()
                    .coordinate(new HashMap<>(coord)).boost(getRandomBoost()).product(getRandomProduct()).build());
        }
        chunkConfig.set("chunks",chunkDataGenerated);
        DataManager.saveAll();
        DataManager.reload();
    } // 청크 생성 함수
    private static List<String> getRandomBoost(){
        Random random = new Random();
        List<String> a = List.of("none");
        List<String> b = List.of("prodamp-1");
        List<String> c = List.of("huntamp-1");
        List<String> output = List.of();
        output = switch (random.nextInt(3)) {
            case 0 -> a;
            case 1 -> b;
            case 2 -> c;
            default -> output;
        };
        return output;
    }
    private static List<String> getRandomProduct(){
        Random random = new Random();
        List<String> a = List.of("IRON-1");
        List<String> b = List.of("PAPER-1");
        List<String> c = List.of("WHEAT-1");
        List<String> output = List.of();
        output = switch (random.nextInt(3)) {
            case 0 -> a;
            case 1 -> b;
            case 2 -> c;
            default -> output;
        };
        return output;
    }
    private static class ChunkProperties {
        public Map<String,Integer> coordinate;
        public int level;
        public String status;
        public String claimer;
        public List<String> boost; // default : {prodamp-1, tradeadvantage-1}
        public List<String> product; // default : {iron-1, paper-1}

        public ChunkProperties(Builder builder) {
            this.coordinate = builder.coordinate;
            this.status = builder.status;
            this.claimer = builder.claimer;
            this.level = builder.level;
            this.boost = builder.boost;
            this.product = builder.product;
        }
        public static class Builder {
            private Map<String,Integer> coordinate;
            private int level = 1; // default : 1
            private String status = "unclaimed"; // border, safezone, claimed, unclaimed
            private String claimer = "none"; // default : none
            private List<String> boost;
            private List<String> product;

            public Builder coordinate(Map<String,Integer> coordinate) {
                this.coordinate = coordinate;
                return this;
            }

            public Builder boost(List<String> boost) {
                this.boost = boost;
                return this;
            }

            public Builder product(List<String> product) {
                this.product = product;
                return this;
            }

            public Builder level(int level) {
                this.level = level;
                return this;
            }

            public Builder status(String status) {
                this.status = status;
                return this;
            }

            public Builder claimer(String claimer) {
                this.claimer = claimer;
                return this;
            }

            public Map<String, Object> build() {
                Map<String, Object> map = new HashMap<>();
                map.put("coordinate", coordinate);
                map.put("level", level);
                map.put("status", status);
                map.put("claimer", claimer);
                map.put("boost", boost);
                map.put("product", product);
                return map;
            }
        }
    } // 클래스 build 구조

    public static int getChunkIndex(int x, int z){
        FileConfiguration chunkConfig = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConfig.getConfigurationSection("chunks");
        if (chunks == null) {
            return 0;
        }
        for (String key : chunks.getKeys(false)) {
            ConfigurationSection chunk = chunks.getConfigurationSection(key);
            if (chunk == null) continue;
            int chunkX = chunk.getInt("coordinate.x");
            int chunkZ = chunk.getInt("coordinate.z");

            if (x == chunkX && z == chunkZ) {
                int index = 0;
                try {
                    index = Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    MineCivilization.infoLog("숫자가 아닌 값을 입력했습니다.");
                }
                return index;
            }
        }
        MineCivilization.infoLog("현재 위치한 청크를 찾을 수 없습니다.");
        return 0;
    }
    public static ChunkCoordinate getChunkCoordinate(int index){
        FileConfiguration chunkConfig = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConfig.getConfigurationSection("chunks");
        if (chunks == null) {
            MineCivilization.infoLog("청크 데이터가 없습니다.");
            return new ChunkCoordinate(0, 0);
        }
        for (String key : chunks.getKeys(false)) {
            ConfigurationSection chunk = chunks.getConfigurationSection(key);
            if (chunk == null) continue;
            int chunkX = chunk.getInt("coordinate.x");
            int chunkZ = chunk.getInt("coordinate.z");

            if (key.equals(Integer.toString(index))) {
                return new ChunkCoordinate(chunkX, chunkZ);
            }
        }
        MineCivilization.infoLog("현재 위치한 청크를 찾을 수 없습니다.");
        return new ChunkCoordinate(0, 0);
    }
    public record ChunkCoordinate(int x, int z) {}
    public static ChunkProperty getChunkProperty(int index){
        FileConfiguration chunkConfig = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConfig.getConfigurationSection("chunks");

        if (chunks == null) {
            MineCivilization.infoLog("청크 데이터가 없습니다.");
            return new ChunkProperty(0, 0, 0, "unclaimed", "none", List.of(""), List.of(""));
        }//예외처리
        for (String key : chunks.getKeys(false)) {
            ConfigurationSection chunk = chunks.getConfigurationSection(key);
            if (chunk == null) continue;
            int level = chunk.getInt("level");
            int chunkX = chunk.getInt("coordinate.x");
            int chunkZ = chunk.getInt("coordinate.z");
            String status = chunk.getString("status", "none");
            String claimer = chunk.getString("claimer", "none");
            List<String> product = chunk.getStringList("product");
            List<String> boost = chunk.getStringList("boost");
            if (key.equals(Integer.toString(index))) {
                return new ChunkProperty(level, chunkX, chunkZ, status, claimer, product, boost);
            }
        }
        {
            MineCivilization.infoLog("현재 위치한 청크를 찾을 수 없습니다.");
            return new ChunkProperty(0, 0, 0, "unclaimed", "none", List.of(""), List.of(""));
        }//예외처리
    }
    public record ChunkProperty(int level, int x, int z, String status, String claimer, List<String> product, List<String> boost){}

    public static boolean setChunkProperty(int index, String target, Object input){
        FileConfiguration chunkConfig = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConfig.getConfigurationSection("chunks");
        ChunkProperty chunk = getChunkProperty(index);
        if (chunks == null){return false;}
        chunks.set(index+target,input);
        chunkConfig.set("chunks",chunks);
        DataManager.saveAll();
        DataManager.reload();
        return true;
    }
}
