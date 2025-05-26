package io.github.surang_volkov.minecivilization.events.GUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class GuildOwnerEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("길드장 메뉴")) {
            e.setCancelled(true);
            if (Objects.requireNonNull(e.getCurrentItem()).getType() == Material.AIR && e.getCurrentItem() != null){

            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1345) {
                Player player = (Player) e.getWhoClicked();
                // 길드장 위임
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1346) {
                Player player = (Player) e.getWhoClicked();
                // 부길드장 임명
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1347) {
                Player player = (Player) e.getWhoClicked();
                // 부길드장 해임
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1348) {
                Player player = (Player) e.getWhoClicked();
                // 길드 해산
            }
        }

    }
}
