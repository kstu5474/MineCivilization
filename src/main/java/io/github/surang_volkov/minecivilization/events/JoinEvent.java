package io.github.surang_volkov.minecivilization.events;

import io.github.surang_volkov.minecivilization.tools.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.sendMessage("Hello, "+p.getName()+"! Enjoy your stay!");
        if(UserManager.checkIfNewUser(p)){
            p.sendMessage("처음 오셨군요! 원하시는 길드에 가입해주세요!");
        }
    }
}
