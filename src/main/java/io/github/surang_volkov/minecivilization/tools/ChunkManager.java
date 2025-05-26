package io.github.surang_volkov.minecivilization.tools;

import io.github.surang_volkov.minecivilization.MineCivilization;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static io.github.surang_volkov.minecivilization.tools.DataManager.getChunkConfig;

public class ChunkManager {
    public static void createChunkProperties(Map<String,Integer> coordinate, int limit){
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
    } //좌표에서 인덱스를 반환하는 메서드. 경계선 바깥의 정의되지 않은 청크는 항상 index = 0이 반환됨.

    public static Optional<ChunkCoordinate> getChunkCoordinate(int index){
        FileConfiguration chunkConfig = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConfig.getConfigurationSection("chunks");
        if (chunks == null) {
            MineCivilization.infoLog("청크 데이터가 없습니다.");
            return Optional.empty();
        }
        for (String key : chunks.getKeys(false)) {
            ConfigurationSection chunk = chunks.getConfigurationSection(key);
            if (chunk == null) continue;
            int chunkX = chunk.getInt("coordinate.x");
            int chunkZ = chunk.getInt("coordinate.z");

            if (key.equals(Integer.toString(index))) {
                return Optional.of(new ChunkCoordinate(chunkX, chunkZ));
            }
        }
        MineCivilization.infoLog("현재 위치한 청크를 찾을 수 없습니다.");
        return Optional.empty();
    } // 인덱스에서 좌표를 반환하는 메서드. 0이나 존재하지 않는 index를 입력하면 empty()가 반환됨. 사용처에서 .isEmpty()로 확인할 수 있음
    public record ChunkCoordinate(int x, int z) {}

    public static Optional<ChunkProperty> getChunkProperty(int index){
        FileConfiguration chunkConfig = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConfig.getConfigurationSection("chunks");
        if (chunks == null) {
            MineCivilization.infoLog("청크 데이터가 없습니다.");
            return Optional.empty(); // ❗ 데이터 자체가 없는 경우
        }
        for (String key : chunks.getKeys(false)) {
            if (key.equals(Integer.toString(index))) {
                ConfigurationSection chunk = chunks.getConfigurationSection(key);
                if (chunk == null) continue;
                int level = chunk.getInt("level");
                int chunkX = chunk.getInt("coordinate.x");
                int chunkZ = chunk.getInt("coordinate.z");
                String status = chunk.getString("status", "none");
                String claimer = chunk.getString("claimer", "none");
                List<String> product = chunk.getStringList("product");
                List<String> boost = chunk.getStringList("boost");
                return Optional.of(new ChunkProperty(level, chunkX, chunkZ, status, claimer, product, boost));
            }
        }
        MineCivilization.infoLog("현재 위치한 청크를 찾을 수 없습니다.");
        return Optional.empty(); // ❗ 청크를 찾을 수 없는 경우
    } // 인덱스의 내용을 반환하는 메서드. 0이나 존재하지 않는 index를 입력하면 empty()가 반환됨. 사용처에서 .isEmpty()로 확인할 수 있음
    public record ChunkProperty(int level, int x, int z, String status, String claimer, List<String> product, List<String> boost){}

    public static boolean setChunkProperty(int index, String target, Object input){
        FileConfiguration chunkConfig = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConfig.getConfigurationSection("chunks");
        if(chunks == null) return false;
        chunks.set(index+"."+target,input);//로직 미완성
        DataManager.reload();
        return true;
    }//성공하면 true 반환. 파일 저장,리로드 까지 진행 (되도록 이 함수를 직접 쓰지 말것)

    public static boolean isTerritoryNearbyGuild(String guildName,int index){
        if (getChunkProperty(index).isEmpty()) {MineCivilization.infoLog(""); return false;}
        Optional<ChunkCoordinate> coord = getChunkCoordinate(index);
        if(coord.isEmpty()) return false;
        int x = coord.get().x();
        int z = coord.get().z();
        boolean canClaim = false;
        Optional<ChunkProperty> posX = getChunkProperty(getChunkIndex(x+1,z));
        Optional<ChunkProperty> negX = getChunkProperty(getChunkIndex(x-1,z));
        Optional<ChunkProperty> posZ = getChunkProperty(getChunkIndex(x,z+1));
        Optional<ChunkProperty> negZ = getChunkProperty(getChunkIndex(x,z-1));
        if(posX.isPresent() && Objects.equals(posX.get().claimer(),guildName)) canClaim = true;
        if(negX.isPresent() && Objects.equals(negX.get().claimer(),guildName)) canClaim = true;
        if(posZ.isPresent() && Objects.equals(posZ.get().claimer(),guildName)) canClaim = true;
        if(negZ.isPresent() && Objects.equals(negZ.get().claimer(),guildName)) canClaim = true;
        return canClaim;
    }//단순 계산 메서드. 인접 청크가 해당길 드의 영토인지 확인
}
