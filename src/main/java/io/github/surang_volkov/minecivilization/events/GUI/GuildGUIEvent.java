package io.github.surang_volkov.minecivilization.events.GUI;

import io.github.surang_volkov.minecivilization.gui.GuildGUI;
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
            }
        }
    }
}
