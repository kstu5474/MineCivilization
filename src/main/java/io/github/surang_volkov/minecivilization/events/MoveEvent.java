package io.github.surang_volkov.minecivilization.events;

import io.github.surang_volkov.minecivilization.tools.ChunkManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Optional;

public class MoveEvent implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Chunk fromChunk = e.getFrom().getChunk();
        Chunk toChunk = e.getTo().getChunk();
        int fromChunkX = fromChunk.getX();
        int fromChunkZ = fromChunk.getZ();
        int toChunkX = toChunk.getX();
        int toChunkZ = toChunk.getZ();
        //직전 청크와 다음 청크가 다르다면
        if ( !List.of(fromChunkX, fromChunkZ).equals(List.of(toChunkX, toChunkZ)) ) {
            int toIndex = ChunkManager.getChunkIndex(toChunkX,toChunkZ);
            Optional<ChunkManager.ChunkProperty> chunkP = ChunkManager.getChunkProperty(toIndex);
            if(chunkP.isEmpty()){
                p.sendMessage("[MineCiv] 이동 이벤트 발생, 사용할 수 없는 청크입니다.");
            }
            if(chunkP.isPresent()){
                String claimer = chunkP.get().claimer();
                p.sendMessage("[MineCiv] 이동 이벤트 발생!: to index-"+toIndex+" claimed by "+claimer);
            }
            //인덱스 변화에 메시지 전송
        }
    }
}