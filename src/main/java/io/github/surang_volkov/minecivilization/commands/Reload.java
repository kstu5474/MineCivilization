package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.tools.DataManager;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.command.CommandSender;

import static io.github.surang_volkov.minecivilization.MineCivilization.instance;

public class Reload implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        DataManager.saveAll(instance);
        DataManager.reload(instance);
        commandSender.sendMessage("설정 파일이 성공적으로 리로드되었습니다.");
    }
}
