package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerManager {
    public boolean uuidCheck(String name){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        UUID uuid = offlinePlayer.getUniqueId();
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getUniqueId().equals(uuid)){
                return true;
            }
        }
        return false;
    }
}
