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

    public static void init(JavaPlugin plugin){
        generalConfig = load(plugin, "generalConfig.yml");
        chunkConfig = load(plugin, "chunkData.yml");
        guildsConfig = load(plugin, "guildsData.yml");
        userConfig = load(plugin, "userData.yml");
    }
    public static void reload() {
        generalConfig = load(instance, "generalConfig.yml");
        chunkConfig = load(instance, "chunkData.yml");
        guildsConfig = load(instance, "guildsData.yml");
        userConfig = load(instance, "userData.yml");

        infoLog("설정 파일들이 성공적으로 리로드되었습니다.");
    }
    private static FileConfiguration load(JavaPlugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }
    //접근자 (예: 외부에서 가져올 수 있게)
    public static FileConfiguration getGeneralConfig() {
        return generalConfig;
    }
    public static FileConfiguration getChunkConfig() {
        return chunkConfig;
    }
    public static FileConfiguration getGuildsConfig() {
        return guildsConfig;
    }
    public static FileConfiguration getUserConfig() {
        return userConfig;
    }
    //전체 저장 메서드
    public static void saveAll() {
        save(instance, generalConfig, "generalConfig.yml");
        save(instance, chunkConfig, "chunkData.yml");
        save(instance, guildsConfig, "guildsData.yml");
        save(instance, userConfig, "userData.yml");
    }
    private static void save(JavaPlugin plugin, FileConfiguration config, String fileName) {
        try {
            config.save(new File(plugin.getDataFolder(), fileName));
            infoLog(fileName+" 파일 저장 완료");
        } catch (IOException e) {
            errorLog(fileName+" 파일 저장을 실패했습니다.");
            errorLog(e.getMessage());
        }
    }
}
