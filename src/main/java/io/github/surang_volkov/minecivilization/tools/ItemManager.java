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









}

