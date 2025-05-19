package io.github.surang_volkov.minecivilization.tools;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandCompleter implements TabCompleter {

    private static final FileConfiguration guildsConfig = DataManager.getGuildsConfig();
    private static final FileConfiguration chunkConfig = DataManager.getChunkConfig();
    private static final FileConfiguration userConfig = DataManager.getUserConfig();


    private final List<String> sub1 = Arrays.asList("reload","chunk","guild","rules","test");
    private final List<String> subChunk = Arrays.asList("edit","setorigin");
    private final List<String> subChunkEditIndex = Arrays.asList("claimer","level","boost","products");
    private final List<String> subGuild = Arrays.asList("claim","disclaim","invite", "join", "leave", "promote", "demote", "mandate");


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(args.length == 1){
            List<String> completions = new ArrayList<>();
            for(String sub : sub1){
                if(sub.startsWith(args[0].toLowerCase())){
                    completions.add(sub);
                }
            }
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("chunk")){
            List<String> completions = new ArrayList<>();
            for(String sub : subChunk){
                if(sub.startsWith(args[1].toLowerCase())){
                    completions.add(sub);
                }
            }
            return completions;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("chunk") && args[1].equalsIgnoreCase("edit")){
            try {
                return new ArrayList<>(Objects.requireNonNull(chunkConfig.getConfigurationSection("chunks")).getKeys(false));
            } catch (NullPointerException e){
                return List.of("<none>");
            }
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("chunk") && args[1].equalsIgnoreCase("edit") && isInteger(args[2])){
            List<String> completions = new ArrayList<>();
            for(String sub : subChunkEditIndex){
                if(sub.startsWith(args[3].toLowerCase())){
                    completions.add(sub);
                }
            }
            return completions;
        }

        //
        List<String> guildList;
        String guildName;
        List<String> guildMembersList;

        try{
            guildList = new ArrayList<>(Objects.requireNonNull(guildsConfig.getConfigurationSection("guilds")).getKeys(false));
            guildName = userConfig.getString("users."+commandSender.getName()+".guild");
            guildMembersList = guildsConfig.getStringList("guilds."+guildName+".members");
        } catch (NullPointerException e){
            guildMembersList = List.of("<none>");
            guildName = "<none>";
            guildList = List.of("<none>");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("guild")){
            List<String> completions = new ArrayList<>();
            for(String sub : subGuild){
                if(sub.startsWith(args[1].toLowerCase())){
                    completions.add(sub);
                }
            }
            return completions;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("guild") && args[1].equalsIgnoreCase("invite")){
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("guild") && args[1].equalsIgnoreCase("join")){
            return guildList;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("guild") && args[1].equalsIgnoreCase("promote")){
            return guildMembersList;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("guild") && args[1].equalsIgnoreCase("demote")){
            return guildMembersList;
        }
        return List.of();
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
