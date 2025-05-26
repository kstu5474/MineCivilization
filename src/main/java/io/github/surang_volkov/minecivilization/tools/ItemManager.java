package io.github.surang_volkov.minecivilization.tools;

import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import java.util.List;

public class ItemManager {
    public static ItemStack buildItem(Material type, int amount, int maxStackSize, String displayName, int customData, Component... lore){
        ItemStack stack = new ItemStack(type, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setMaxStackSize(maxStackSize);
        meta.displayName(Component.text(displayName).color(NamedTextColor.RED));
        if (lore != null){
            List<Component> lores = List.of(lore);
            meta.lore(lores);
        }
        if (customData != 0){
            meta.setCustomModelData(customData);
        }
        stack.setItemMeta(meta);
        return stack;
    }
    public static ItemStack buildItem(Material type, int amount, int maxStackSize, String displayName, Component... lore) {
        return buildItem(type, amount, maxStackSize, displayName, 0, lore);
    }

    public static final ItemStack testItem = buildItem(Material.BUCKET, 1, 64, "Test Bucket",
            Component.text("This, is a bucket.").color(NamedTextColor.AQUA),
            Component.text("Dear, god...").color(NamedTextColor.LIGHT_PURPLE),
            Component.text("There's more.").color(NamedTextColor.AQUA),
            Component.text("No...").color(NamedTextColor.LIGHT_PURPLE));

    public static ItemStack guildItem = buildItem(Material.DIAMOND,1,1,"§b길드 메뉴",1234,
            Component.text("길드 메뉴를 엽니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildItem01 = buildItem(Material.TOTEM_OF_UNDYING,1,1,"§b길드장 메뉴",1235,
            Component.text("길드장 메뉴로 이동합니다. 길드장만 가능합니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildOwnerItem01 = buildItem(Material.PAPER,1,1,"§b길드장 위임",1345,
            Component.text("길드장을 타 플레이어로 임명합니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildOwnerItem02 = buildItem(Material.ENCHANTED_BOOK,1,1,"§b부길드장 임명",1346,
            Component.text("해당 유저에게 부길드장을 임명합니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildOwnerItem03 = buildItem(Material.IRON_SWORD,1,1,"§b부길드장 해임",1347,
            Component.text("부길드장을 해임합니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildOwnerItem04 = buildItem(Material.BARRIER,1,1,"§b길드 해산",1348,
            Component.text("길드를 해산합니다. 되돌릴 수 없습니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildItem02 = buildItem(Material.ANVIL,1,1,"§b길드 생성",1236,
            Component.text("길드를 생성합니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildItem03 = buildItem(Material.BARRIER,1,1,"§b길드 탈퇴",1237,
            Component.text("길드를 탈퇴합니다. 되돌릴 수 없습니다.").color(NamedTextColor.YELLOW));

    public static ItemStack guildItem04 = buildItem(Material.CYAN_BANNER,1,1,"§b길드 가입",1238,
            Component.text("길드에 가입합니다. 길드장이 수락하여야 합니다.").color(NamedTextColor.YELLOW));

    public static ItemStack TestPlayerhead = buildItem(Material.PLAYER_HEAD,1,1,"§b플레이어 리스트",1123,
            Component.text("T E S  T                      임").color(NamedTextColor.YELLOW));









}

