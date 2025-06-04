package io.github.surang_volkov.minecivilization.events;

import io.github.surang_volkov.minecivilization.gui.MainGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GUIOpeningEvent implements Listener {


    private final Set<UUID> sneakingPlayers = new HashSet<>();

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking()) {
            sneakingPlayers.add(player.getUniqueId());
        } else {
            sneakingPlayers.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (sneakingPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);

            MainGUI inv = new MainGUI();
            inv.open(player);
        }
    }
}
