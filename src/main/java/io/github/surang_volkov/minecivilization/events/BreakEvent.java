package io.github.surang_volkov.minecivilization.events;

import io.github.surang_volkov.minecivilization.tools.ItemManager;
import static io.github.surang_volkov.minecivilization.MineCivilization.infoLog;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakEvent implements Listener {

    @EventHandler
    public void breakDirt(BlockBreakEvent e){
        Material brokenBlock = e.getBlock().getBlockData().getMaterial();
        if (brokenBlock == Material.DIRT || brokenBlock == Material.GRASS_BLOCK){
            if (Math.floor(Math.random() * 10) < 5){
                Player p = e.getPlayer();
                p.getInventory().addItem(ItemManager.testItem);
                p.sendMessage("[MineCiv] Yay! You found a random stuff!");
                infoLog(p.getName()+" has found the testItem at "+e.getBlock().getLocation());
            }
        }
    }
}