package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.tools.ChunkManager;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import static io.github.surang_volkov.minecivilization.MineCivilization.infoLog;

public class Guild implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("§e사용법: /mcv chunk <edit|setorigin>");
        }
        switch (args[0].toLowerCase()){
            case "edit":
                commandSender.sendMessage("§e사용법: /mcv chunk edit <index> <attributes> <arguments>");
                break;
            case "setorigin":
                if (args.length == 1) {
                    commandSender.sendMessage("§e사용법: /mcv chunk setorigin <limit>");
                    return;
                }
                if (args.length == 2) {
                    return;
                }
                break;
        }
    }
}
