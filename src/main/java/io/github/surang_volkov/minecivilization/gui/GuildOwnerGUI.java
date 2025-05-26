package io.github.surang_volkov.minecivilization.gui;

import io.github.surang_volkov.minecivilization.tools.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;


public class GuildOwnerGUI implements Listener {
    private final Inventory GuildOwnerInv;


    private void initItemSetting() {
        GuildOwnerInv.setItem(1,ItemManager.guildOwnerItem01);
        GuildOwnerInv.setItem(3,ItemManager.guildOwnerItem02);
        GuildOwnerInv.setItem(5,ItemManager.guildOwnerItem03);
        GuildOwnerInv.setItem(7,ItemManager.guildOwnerItem04);
    }

    public GuildOwnerGUI() {
        this.GuildOwnerInv = Bukkit.createInventory(null,9,"길드장 메뉴");
        initItemSetting();

    }

    public void open(Player player){
        player.openInventory(GuildOwnerInv);
    }

}
