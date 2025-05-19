package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    void execute(CommandSender commandSender, String[] args);
}
