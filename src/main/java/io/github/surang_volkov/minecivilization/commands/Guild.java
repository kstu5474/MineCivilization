package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.tools.ChunkManager;
import io.github.surang_volkov.minecivilization.tools.GuildManager;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.surang_volkov.minecivilization.MineCivilization.infoLog;

public class Guild implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("§e사용법: /mcv guild <invite|leave|kick|status|create|mandate|promote|demote|delete>");
        }
        switch (args[0].toLowerCase()){
            case "invite":
                commandSender.sendMessage("§e사용법: /mcv guild create <(띄어쓰기 없이)길드이름>");
                break;
            case "leave":
                break;
            case "kick":
                break;
            case "status":
                break;

            case "create":
                if (args.length == 1) {
                    commandSender.sendMessage("§e사용법: /mcv guild create <(띄어쓰기 없이)길드이름>");
                    return;
                }
                if (args.length == 2) {
                    Player p = (Player) commandSender;
                    GuildManager.CreateGuild(p,args[1]);
                    return;
                }
                break;

            case "mandate":
                if (args.length == 1) {
                    commandSender.sendMessage("§e사용법: 다른 유저에게 길드장의 권한을 위임합니다. /mcv guild mandate <유저이름>");
                    return;
                }
                if (args.length == 2) {
                    commandSender.sendMessage("§e정말로 위임하시겠습니까?\n동의하시면 /mcv guild mandate <유저이름> confirm 을 입력해주세요.");
                    return;
                }
                if (args.length == 3) {
                    return;
                }
                break;

            case "promote":
                if (args.length == 1) {
                    commandSender.sendMessage("§e사용법: 부길드장을 임명합니다. /mcv guild promote <유저이름>");
                    return;
                }
                if (args.length == 2) {
                return;
                }
                break;

            case "demote":
                if (args.length == 1) {
                    commandSender.sendMessage("§e사용법: 부길드장을 해임합니다. /mcv guild demote <유저이름>");
                    return;
                }
                if (args.length == 2) {
                    return;
                }
                break;

            case "delete":
                if (args.length == 1) {
                    commandSender.sendMessage("§e사용법: /mcv guild delete <길드이름>");
                    return;
                }
                if (args.length == 2) {
                    commandSender.sendMessage("§e정말 길드를 해산하시겠습니까?\n§e동의하시면 /mcv guild delete <길드이름> confirm 을 입력해주세요.");
                    return;
                }
                if (args.length == 3) {
                    return;
                }
                break;
        }
    }
}
