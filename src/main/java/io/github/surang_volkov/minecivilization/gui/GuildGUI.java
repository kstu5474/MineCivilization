package io.github.surang_volkov.minecivilization.gui;

import io.github.surang_volkov.minecivilization.tools.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;


public class GuildGUI implements Listener {
    private final Inventory GuildInv;


    private void initItemSetting() {
        GuildInv.setItem(1,ItemManager.guildItem02);
        GuildInv.setItem(3,ItemManager.guildItem04);
        GuildInv.setItem(5,ItemManager.guildItem03);
        GuildInv.setItem(7,ItemManager.guildItem01);
    }

    public GuildGUI() {
        this.GuildInv = Bukkit.createInventory(null,9,"길드 메뉴");
        initItemSetting();

    }

    public void open(Player player){
        player.openInventory(GuildInv);
    }

}
