package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.tools.DataManager;
import io.github.surang_volkov.minecivilization.tools.ChunkManager;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

import static io.github.surang_volkov.minecivilization.MineCivilization.infoLog;

public class Chunk implements SubCommand {
    private static final FileConfiguration generalConfig = DataManager.getGeneralConfig();

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
                    if (commandSender instanceof Player p) {
                        Location l = p.getLocation();
                        int limit = Integer.parseInt(args[1]);
                        if (limit % 2 == 1) {
                            generalConfig.set("chunk-config.origin.x",l.getChunk().getX());
                            generalConfig.set("chunk-config.origin.z",l.getChunk().getZ());
                            generalConfig.set("chunk-config.limit",limit);
                            ChunkManager.GenerateChunkProperties(Map.of("x", l.getChunk().getX(),"z", l.getChunk().getZ()), limit);
                            infoLog("청크 속성을 성공적으로 생성했습니다.");
                            commandSender.sendMessage("청크 속성을 성공적으로 생성했습니다.");
                            return;
                        } else if (limit % 2 == 0) {commandSender.sendMessage("§elimit가 짝수입니다. limit 값은 홀수인 정수만 사용가능합니다."); return;}
                    } else {
                        commandSender.sendMessage("§e플레이어만 사용가능한 명령어입니다.");
                        return;
                    }
                }
                break;
        }
    }
}
