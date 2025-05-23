package io.github.surang_volkov.minecivilization.gui;

import io.github.surang_volkov.minecivilization.tools.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class GuildGUI implements Listener {
    private final Inventory Guildinv;


    private void initItemSetting() {
        Guildinv.setItem(1,ItemManager.guildItem01);
    }

    public GuildGUI() {
        this.Guildinv = Bukkit.createInventory(null,9,"길드 메뉴");
        initItemSetting();
    }

    public void open(Player player){
        player.openInventory(Guildinv);
    }

}
