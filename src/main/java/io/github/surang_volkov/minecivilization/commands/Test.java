package io.github.surang_volkov.minecivilization.commands;

import io.github.surang_volkov.minecivilization.MineCivilization;
import io.github.surang_volkov.minecivilization.tools.ChunkManager;
import io.github.surang_volkov.minecivilization.tools.DataManager;
import io.github.surang_volkov.minecivilization.tools.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        MineCivilization.infoLog("테스트 명령어 수신됨");
        if (commandSender instanceof Player p) {
            if(openMapImage(p)){
                commandSender.sendMessage("테스트 성공");
                commandSender.sendMessage("[MineCiv] 현재 시간: "+getTime()+", 현재 위치 정보: "+ getShit(p));

            }
        } else {
            MineCivilization.infoLog("관리자 플레이어만 이 메시지를 사용할 수 있습니다.");
            commandSender.sendMessage("관리자 플레이어만 이 메시지를 사용할 수 있습니다.");
        }
    }

    private boolean openMapImage(Player p){
        int size = 5; // 청크 좌표 범위에 따라 결정
        int pixelPerChunk = 32;
        BufferedImage image = new BufferedImage(size * pixelPerChunk, size * pixelPerChunk, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setFont(new Font("Arial", Font.PLAIN, 8));

        FileConfiguration chunkConf = DataManager.getChunkConfig();
        ConfigurationSection chunks = chunkConf.getConfigurationSection("chunks");
        if(chunks == null) return false;
        for (String i : chunks.getKeys(false)) {
            int index = Integer.parseInt(i);
            Optional<ChunkManager.ChunkProperty> chunk = ChunkManager.getChunkProperties(index);
            if(chunk.isEmpty()) return false;
            int cx = chunk.get().x() + 2; // 좌표 기준점 보정
            int cz = chunk.get().z() + 2;

            int x = cx * pixelPerChunk;
            int y = cz * pixelPerChunk;

            Color color = Color.LIGHT_GRAY;
            if (chunk.get().status().equals("claimed")) {
                color = Color.CYAN;
            }

            g.setColor(color);
            g.fillRect(x, y, pixelPerChunk, pixelPerChunk);

            g.setColor(Color.BLACK);
            g.drawString(i, x + 2, y + 12);
            g.drawString(chunk.get().product().toString().split("-")[0], x + 2, y + 24);
        }
        g.dispose();

        try {
            File imageFile = new File(MineCivilization.instance.getDataFolder(), "map.png");
            ImageIO.write(image, "png", imageFile);
            // 1. 이미지 불러오기
            if (!imageFile.exists()) {
                p.sendMessage("§c지도를 생성하지 못했습니다. 이미지 파일이 없습니다.");
                return false;
            }
            BufferedImage bufferedImage = ImageIO.read(imageFile);

            // 2. MapView 생성
            MapView mapView = Bukkit.createMap(p.getWorld());
            mapView.getRenderers().clear(); // 기본 렌더러 제거

            // 3. 익명 MapRenderer 추가
            mapView.addRenderer(new MapRenderer(false) {
                private boolean rendered = false;
                @Override
                public void render(@NotNull MapView view, @NotNull MapCanvas canvas, @NotNull Player player) {
                    if (rendered) return; // 한 번만 렌더링
                    Image scaled = bufferedImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                    canvas.drawImage(0, 0, scaled);
                    rendered = true;
                }
            });

            // 4. 지도 아이템 생성 및 MapView 바인딩
            ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) mapItem.getItemMeta();
            meta.setMapView(mapView);
            meta.displayName(Component.text("a"));
            mapItem.setItemMeta(meta);

            // 5. gui에 띄우기
            Inventory gui = Bukkit.createInventory(null, 18, Component.text("영토 지도", NamedTextColor.GOLD));
            gui.setItem(13, mapItem);
            p.openInventory(gui);
        } catch (IOException e) {
            MineCivilization.errorLog("이미지 생성 중 문제가 발생했습니다.");
            return false;
        }
        return true;
    }


    private String getShit(Player p){
        int playerX = p.getChunk().getX();
        int playerZ = p.getChunk().getZ();
        int key = ChunkManager.getChunkIndex(playerX,playerZ);
        Optional<ChunkManager.ChunkCoordinate> coord = ChunkManager.getChunkCoordinate(key);
        Optional<ChunkManager.ChunkProperty> chunkP = ChunkManager.getChunkProperties(key);
        if (coord.isEmpty() || chunkP.isEmpty()) return "알수없음";
        String claimer = chunkP.get().claimer();
        return "index-" + key + ", coordinate-(" + coord.get().x() + ", " + coord.get().z() + "), claimedby-" + claimer;
    }

    public String getTime(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }//시간확인함수
}
