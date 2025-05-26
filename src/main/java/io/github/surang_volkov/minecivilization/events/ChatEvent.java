package io.github.surang_volkov.minecivilization.events;

import io.github.surang_volkov.minecivilization.MineCivilization;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatEvent implements Listener {
    public static Map<UUID, Consumer<String>> inputMap = new HashMap<>();

    public static void waitForInput(Player p, Consumer<String> handler){
        UUID uuid = p.getUniqueId();
        inputMap.put(uuid, handler);
        Bukkit.getScheduler().runTaskLater(MineCivilization.instance, () -> {
            if (inputMap.containsKey(uuid)) {
                inputMap.remove(uuid);
                p.sendMessage("§c입력이 취소되었습니다. (시간 초과)");
            }
        }, 600L); //30초; 20틱 = 1초
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (inputMap.containsKey(uuid)) {
            e.setCancelled(true); // 일반 채팅 차단
            String message = PlainTextComponentSerializer.plainText().serialize(e.message());
            Consumer<String> handler = inputMap.remove(uuid);

            if (handler != null) {
                Bukkit.getScheduler().runTask(MineCivilization.instance, () -> handler.accept(message));
            }
        }
    }
}
