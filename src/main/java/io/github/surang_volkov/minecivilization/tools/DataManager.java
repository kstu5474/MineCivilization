package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import static io.github.surang_volkov.minecivilization.MineCivilization.errorLog;
import static io.github.surang_volkov.minecivilization.MineCivilization.infoLog;
import static io.github.surang_volkov.minecivilization.MineCivilization.instance;

public class DataManager {
    private static FileConfiguration generalConfig;
    private static FileConfiguration chunkConfig;
    private static FileConfiguration guildsConfig;
    private static FileConfiguration userConfig;
    public static void init(JavaPlugin plugin) {
        generalConfig = load(plugin, "generalConfig.yml");
        chunkConfig = load(plugin, "chunkData.yml");
        guildsConfig = load(plugin, "guildsData.yml");
        userConfig = load(plugin, "userData.yml");
    } //시작할때 로드하는 역할. 처음 한번만 호출
    public static boolean reload() {
        generalConfig = load(instance, "generalConfig.yml");
        chunkConfig = load(instance, "chunkData.yml");
        guildsConfig = load(instance, "guildsData.yml");
        userConfig = load(instance, "userData.yml");
        return true;
    } //저장 및 리로드. 실패하면 false 반환
    private static FileConfiguration load(JavaPlugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    } // 로드할때 사용. 개별적으로는 사용 안함.
    //각 파일마다 config 가져올 때 사용.
    public static FileConfiguration getGeneralConfig() {return generalConfig;}
    public static FileConfiguration getChunkConfig() {return chunkConfig;}
    public static FileConfiguration getGuildsConfig() {return guildsConfig;}
    public static FileConfiguration getUserConfig() {return userConfig;}
    public static boolean save() {
        if (saveOne(instance, generalConfig, "generalConfig.yml")&&
                saveOne(instance, chunkConfig, "chunkData.yml")&&
                saveOne(instance, guildsConfig, "guildsData.yml")&&
                saveOne(instance, userConfig, "userData.yml")){
            return true;
        }else return false;
    } //전체 저장 메서드
    private static boolean saveOne(JavaPlugin plugin, FileConfiguration config, String fileName) {
        try {
            config.save(new File(plugin.getDataFolder(), fileName));
            return true;
        } catch (IOException e) {
            errorLog(fileName+" 파일 저장을 실패했습니다.");
            errorLog(e.getMessage());
            return false;
        }
    } // 개별 저장 메서드. 개별적으로는 사용 안함.
}
