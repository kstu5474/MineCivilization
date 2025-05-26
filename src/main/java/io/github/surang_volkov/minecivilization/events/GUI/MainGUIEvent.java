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

public class MainGUIEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("메인 메뉴")) {
            e.setCancelled(true);
            if (Objects.requireNonNull(e.getCurrentItem()).getType() == Material.AIR && e.getCurrentItem() != null){
            }
            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1234) {
                Player player = (Player) e.getWhoClicked();
                GuildGUI Guildinv = new GuildGUI();
                Guildinv.open(player);
            }
            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1123){
                
            }
        }
    }
}
