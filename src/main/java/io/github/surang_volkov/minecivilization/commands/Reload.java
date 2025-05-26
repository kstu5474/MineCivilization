package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.tools.DataManager;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.command.CommandSender;

public class Reload implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        DataManager.reload();
        commandSender.sendMessage("설정 파일이 성공적으로 저장 및 리로드되었습니다.");
    }
}
