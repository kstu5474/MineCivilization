package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.gui.GuildGUI;
import io.github.surang_volkov.minecivilization.gui.MainGUI;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class gui implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        Player player = (Player) commandSender;
        MainGUI inv = new MainGUI();
        inv.open(player);

    }
}
