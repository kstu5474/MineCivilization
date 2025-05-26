package io.github.surang_volkov.minecivilization.gui;

import io.github.surang_volkov.minecivilization.tools.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;


public class MainGUI implements Listener {
    private final Inventory inv;


    private void initItemSetting() {
        inv.setItem(13,ItemManager.guildItem);
        inv.setItem(1,ItemManager.TestPlayerhead);
    }

    public MainGUI() {
        this.inv = Bukkit.createInventory(null,27,"메인 메뉴");
        initItemSetting();
    }

    public void open(Player player){
        player.openInventory(inv);
    }

}
