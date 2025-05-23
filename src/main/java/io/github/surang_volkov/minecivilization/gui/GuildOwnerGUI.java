package io.github.surang_volkov.minecivilization.gui;

import io.github.surang_volkov.minecivilization.tools.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class GuildOwnerGUI implements Listener {
    private final Inventory GuildOwnerinv;


    private void initItemSetting() {
        GuildOwnerinv.setItem(1,ItemManager.guildItem01);
    }

    public GuildOwnerGUI() {
        this.GuildOwnerinv = Bukkit.createInventory(null,9,"길드장 메뉴");
        initItemSetting();
    }

    public void open(Player player){
        player.openInventory(GuildOwnerinv);
    }

}
