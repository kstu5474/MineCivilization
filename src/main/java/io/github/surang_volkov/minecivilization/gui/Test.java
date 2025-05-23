package io.github.surang_volkov.minecivilization.gui;

import io.github.surang_volkov.minecivilization.tools.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class Test implements Listener {
    private final Inventory inv;

    private void initItemSetting() {
        inv.setItem(14,ItemManager.testItem);
    }

    public Test() {
        this.inv = Bukkit.createInventory(null,27,"TEST");
        initItemSetting();
    }

    public void open(Player player){
        player.openInventory(inv);
    }

}
