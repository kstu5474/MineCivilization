package io.github.surang_volkov.minecivilization;

import io.github.surang_volkov.minecivilization.events.GUI.GuildGUIEvent;
import io.github.surang_volkov.minecivilization.events.GUI.GuildOwnerEvent;
import io.github.surang_volkov.minecivilization.events.MoveEvent;
import io.github.surang_volkov.minecivilization.events.GUI.MainGUIEvent;
import io.github.surang_volkov.minecivilization.tools.CommandCompleter;
import io.github.surang_volkov.minecivilization.tools.DataManager;
import io.github.surang_volkov.minecivilization.tools.McvCommand;
import io.github.surang_volkov.minecivilization.events.BreakEvent;
import io.github.surang_volkov.minecivilization.events.JoinEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public class MineCivilization extends JavaPlugin implements Listener {

    public static MineCivilization instance;

    @Override
    public void onEnable() {
        instance = this;
        DataManager.init(this);
        // saveResource("chunkData.yml",false);
        getServer().getPluginManager().registerEvents(new BreakEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        getServer().getPluginManager().registerEvents(new MainGUIEvent(), this);
        getServer().getPluginManager().registerEvents(new GuildGUIEvent(), this);
        getServer().getPluginManager().registerEvents(new GuildOwnerEvent(), this);
        Objects.requireNonNull(getCommand("mcv")).setExecutor(new McvCommand());
        Objects.requireNonNull(getCommand("mcv")).setTabCompleter(new CommandCompleter());
        infoLog("플러그인 로드 완료");
    }
    @Override
    public void onDisable() {
        DataManager.saveAll();
        infoLog("플러그인 비활성화");
    }
    //info 로그 남기는 함수
    public static void infoLog(String msg){
        instance.getLogger().info("[MineCiv] "+msg);
    }
    //error 로그 남기는 함수
    public static void errorLog(String msg){
        instance.getLogger().severe("[MineCiv - FATAL ERROR] "+msg);
    }
}
