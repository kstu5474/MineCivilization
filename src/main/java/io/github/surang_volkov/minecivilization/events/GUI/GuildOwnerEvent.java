package io.github.surang_volkov.minecivilization.events.GUI;

import io.github.surang_volkov.minecivilization.events.ChatEvent;
import io.github.surang_volkov.minecivilization.gui.GuildGUI;
import io.github.surang_volkov.minecivilization.tools.GuildManager;
import io.github.surang_volkov.minecivilization.tools.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.Optional;

public class GuildOwnerEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        GuildGUI GuildInv = new GuildGUI();
        if (e.getClickedInventory() == null)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("길드장 메뉴")) {
            e.setCancelled(true);
            if (Objects.requireNonNull(e.getCurrentItem()).getType() == Material.AIR && e.getCurrentItem() != null){

            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1345) {
                // 길드장 위임
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1346) {
                // 부길드장 임명
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1347) {
                // 부길드장 해임
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1348) {
                p.sendMessage("정말로 삭제하시겠습니까? 동의하시면 길드의 이름을 적어주세요.");
                p.closeInventory();
                ChatEvent.waitForInput(p, input -> {
                    Optional<UserManager.UserProperty> userProp = UserManager.getUserProperty(p.getName());
                    if(userProp.isPresent() && Objects.equals(input,userProp.get().guild())){
                        GuildInv.open(p);
                        if(GuildManager.removeGuild(p, input)) p.sendMessage("길드가 성공적으로 삭제되었습니다.");
                    } else {
                        GuildInv.open(p);
                        p.sendMessage("취소되었습니다.");
                    }
                });
                // 길드 해산
            }
        }

    }
}
