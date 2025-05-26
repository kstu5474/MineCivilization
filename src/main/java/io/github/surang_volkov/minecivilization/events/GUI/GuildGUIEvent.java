package io.github.surang_volkov.minecivilization.events.GUI;


import io.github.surang_volkov.minecivilization.gui.GuildOwnerGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class GuildGUIEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("길드 메뉴")) {
            e.setCancelled(true);
            if (Objects.requireNonNull(e.getCurrentItem()).getType() == Material.AIR && e.getCurrentItem() != null){
            }
            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1235) {
                Player player = (Player) e.getWhoClicked();
                GuildOwnerGUI GuildOwnerinv = new GuildOwnerGUI();
                GuildOwnerinv.open(player);
                // 길드장 구별 넣어야 함
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1236) {
                Player player = (Player) e.getWhoClicked();
                // 길드 생성
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1237) {
                Player player = (Player) e.getWhoClicked();
                //길드 탈퇴
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1238) {
                Player player = (Player) e.getWhoClicked();
                //길드 가입
            }
        }

    }
}
