package io.github.surang_volkov.minecivilization.events;

import io.github.surang_volkov.minecivilization.MineCivilization;
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
        boolean isNewUser = UserManager.isNewUser(p);
        if(isNewUser){
            UserManager.addNewUser(p);
            p.sendMessage("처음 오셨군요! 길드에 꼭 가입해주세요!");
        }else if(!UserManager.isMember(p.getName())){
            p.sendMessage("어서오세요! 가입된 길드가 없으시군요! 길드를 찾아서 가입해주세요!");
        }else p.sendMessage("어서오세요! 항상 서버를 즐겨주셔서 감사합니다!");
    }
}
