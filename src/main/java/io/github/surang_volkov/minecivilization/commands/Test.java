package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.MineCivilization;
import io.github.surang_volkov.minecivilization.tools.ChunkManager;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class Test implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        MineCivilization.infoLog("테스트 명령어 수신됨");
        if (commandSender instanceof Player p) {
            commandSender.sendMessage("[MineCiv] 현재 시간: "+getTime()+", 현재 위치 정보: "+ getShit(p));
        } else {
            MineCivilization.infoLog("관리자 플레이어만 이 메시지를 사용할 수 있습니다.");
            commandSender.sendMessage("관리자 플레이어만 이 메시지를 사용할 수 있습니다.");
        }
    }

    private String getShit(Player p){
        int playerX = p.getChunk().getX();
        int playerZ = p.getChunk().getZ();

        int key = ChunkManager.getChunkIndex(playerX,playerZ);
        ChunkManager.ChunkCoordinate coord = ChunkManager.getChunkCoordinate(key);
        ChunkManager.ChunkProperty chunkP = ChunkManager.getChunkProperty(key);
        String claimer = chunkP.claimer();
        return "index-" + key + ", coordinate-(" + coord.x() + ", " + coord.z() + "), claimedby-" + claimer;
    }
    //시간 확인 함수
    public String getTime(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }
}
