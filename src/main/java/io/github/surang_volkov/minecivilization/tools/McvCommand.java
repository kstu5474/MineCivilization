package io.github.surang_volkov.minecivilization.tools;

import io.github.surang_volkov.minecivilization.commands.*;
import io.github.surang_volkov.minecivilization.commands.gui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class McvCommand implements CommandExecutor {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public McvCommand(){
        subCommands.put("rules",new Rules());
        subCommands.put("reload",new Reload());
        subCommands.put("chunk",new Chunk());
        subCommands.put("guild",new Guild());
        subCommands.put("test",new Test());
        subCommands.put("gui",new gui());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            commandSender.sendMessage("§e사용법: /mcv <reload|chunk|guild|test|rules>");
            return true;
        }
        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub == null) {
            commandSender.sendMessage("§e알수 없는 명령어입니다: " + args[0]);
            return true;
        }
        String[] subargs;
        if (args.length > 1) {subargs = Arrays.copyOfRange(args, 1, args.length);
        } else {
            subargs = new String[0];
        }
        sub.execute(commandSender, subargs);
        return true;
    }
}
