package io.github.surang_volkov.minecivilization.events.GUI;

import io.github.surang_volkov.minecivilization.events.ChatEvent;
import io.github.surang_volkov.minecivilization.gui.GuildGUI;
import io.github.surang_volkov.minecivilization.gui.GuildOwnerGUI;
import io.github.surang_volkov.minecivilization.tools.GuildManager;
import io.github.surang_volkov.minecivilization.tools.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.Optional;

public class GuildOwnerEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        GuildOwnerGUI GuildOwnerInv = new GuildOwnerGUI();
        GuildGUI GuildInv = new GuildGUI();
        if (e.getClickedInventory() == null)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("길드장 메뉴")) {
            e.setCancelled(true);
            if (Objects.requireNonNull(e.getCurrentItem()).getType() == Material.AIR && e.getCurrentItem() != null){

            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1345) {
                p.sendMessage("길드장 자리를 위임할 유저의 이름을 적어주세요.");
                p.closeInventory();
                ChatEvent.waitForInput(p,input -> {

                    Optional<UserManager.UserProperty> targetProp = UserManager.getUserProperties(input);
                    Optional<UserManager.UserProperty> userProp = UserManager.getUserProperties(p.getName());
                    if(targetProp.isPresent() && userProp.isPresent()){
                        if(GuildManager.setGuildLeader(userProp.get().guild(),input)){
                            //재확인 코드 추가할것
                            p.sendMessage("길드장 자리가 위임되었습니다.");
                            GuildInv.open(p);
                        }else{
                            p.sendMessage("현재 길드장 또는 부길드장이거나, 같은 길드원이 아닌 유저입니다.");
                            GuildOwnerInv.open(p);
                        }
                    }else{
                        p.sendMessage("유효한 유저이름이 아닙니다.");
                        GuildOwnerInv.open(p);
                    }

                });
            } // 길드장 위임

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1346) {
                p.sendMessage("부길드장으로 임명할 유저의 이름을 적어주세요.");
                p.closeInventory();
                ChatEvent.waitForInput(p,input -> {

                    Optional<UserManager.UserProperty> targetProp = UserManager.getUserProperties(input);
                    Optional<UserManager.UserProperty> userProp = UserManager.getUserProperties(p.getName());
                    if(targetProp.isPresent() && userProp.isPresent()){
                        Optional<GuildManager.GuildProperty> guildProp = GuildManager.getGuildProperties(userProp.get().guild());
                        if(guildProp.isPresent()){
                            if(guildProp.get().viceLeader().equals("none")){
                                if(GuildManager.setGuildViceLeader(userProp.get().guild(),input)){
                                    p.sendMessage("부길드장이 임명되었습니다.");
                                    GuildOwnerInv.open(p);
                                }else{
                                    p.sendMessage("현재 길드장 또는 부길드장이거나, 같은 길드원이 아닌 유저입니다.");
                                    GuildOwnerInv.open(p);
                                }
                            } else{
                                p.sendMessage("이미 부길드장이 있습니다. 부길드장을 변경하시려면 현재 부길드장을 해임 후 진행해주세요.");
                                GuildOwnerInv.open(p);
                            }
                        }
                    }else{
                        p.sendMessage("유효한 유저이름이 아닙니다.");
                        GuildOwnerInv.open(p);
                    }

                });
            } // 부길드장 임명

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1347) {
                p.sendMessage("정말로 부길드장을 해임하시겠습니까? 동의하시면 confirm을 적어주세요.");
                p.closeInventory();
                ChatEvent.waitForInput(p,input -> {

                    if(input.equals("confirm")){
                        Optional<UserManager.UserProperty> userProp = UserManager.getUserProperties(p.getName());
                        if(userProp.isPresent()){
                            GuildManager.setGuildViceLeader(userProp.get().guild(),"none");
                            p.sendMessage("부길드장이 해임되었습니다.");
                            GuildOwnerInv.open(p);
                        }
                    }

                });
            } // 부길드장 해임

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1348) {
                p.sendMessage("정말로 삭제하시겠습니까? 동의하시면 길드의 이름을 적어주세요.");
                p.closeInventory();
                ChatEvent.waitForInput(p, input -> {

                    Optional<UserManager.UserProperty> userProp = UserManager.getUserProperties(p.getName());
                    if(userProp.isPresent() && Objects.equals(input,userProp.get().guild())){
                        if(GuildManager.removeGuild(p, input)) p.sendMessage("길드가 성공적으로 삭제되었습니다.");
                        GuildInv.open(p);
                    } else {
                        p.sendMessage("취소되었습니다.");
                        GuildOwnerInv.open(p);
                    }

                });

            } // 길드 해산
        }

    }
}
