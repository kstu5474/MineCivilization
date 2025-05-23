package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.gui.MainGUI;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gui implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        Player player = (Player) commandSender;
        MainGUI inv = new MainGUI();
        inv.open(player);

    }
}