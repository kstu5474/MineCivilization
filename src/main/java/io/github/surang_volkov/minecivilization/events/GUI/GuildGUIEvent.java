package io.github.surang_volkov.minecivilization.events.GUI;


import io.github.surang_volkov.minecivilization.MineCivilization;
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

public class GuildGUIEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        GuildOwnerGUI GuildOwnerInv = new GuildOwnerGUI();
        GuildGUI GuildInv = new GuildGUI();
        //변수 정의는 맨 위로 뺐음

        if (e.getClickedInventory() == null)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("길드 메뉴")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null && Objects.requireNonNull(e.getCurrentItem()).getType() == Material.AIR){
                MineCivilization.infoLog("허공 클릭 해서 null 받아짐");
            }

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1235) {
                if(UserManager.isLeader(p.getName())){
                    GuildOwnerInv.open(p);
                }//길드장 확인 로직 후 gui 열기
            } //길드장 gui 열기

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1236) {
                p.sendMessage("생성할 길드의 이름을 적어주세요.(영어 소문자만 가능)");
                p.closeInventory();
                ChatEvent.waitForInput(p,input -> {
                    Optional<UserManager.UserProperty> userProp = UserManager.getUserProperties(p.getName());
                    if(userProp.isPresent() && userProp.get().status().equals("independent")){
                        if(!GuildManager.isGuild(input)){
                            if (GuildManager.CreateGuild(p, input)) {
                                p.sendMessage(input + " 길드를 성공적으로 생성했습니다!");
                                GuildOwnerInv.open(p); // 생성했으면 오너 gui 열기
                            }
                        } else {
                            GuildInv.open(p);
                            p.sendMessage("이미 존재하는 길드이거나, 사용할 수 없는 길드이름입니다. 또는 길드를 생성하는데 실패했을 수도 있습니다.");
                        }
                    }else {
                        GuildInv.open(p);
                        p.sendMessage("이미 길드에 가입되어 있습니다.");
                    }

                });
                // ChatEvent.waitForInput(p,input -> {});
                // 입력 대기하는 로직임 참고바람 **

                // 수랑: 길드 gui는 길드 제목을 메뉴에 띄우는 게 좋을 것 같음
            } // 길드 생성

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1237) {

            }//길드 탈퇴

            else if (e.getCurrentItem().getItemMeta().getCustomModelData() == 1238) {
                p.sendMessage("가입할 길드의 이름을 적어주세요.(영어 소문자만 가능)");
                p.closeInventory();
                ChatEvent.waitForInput(p,input -> {
                    if(!UserManager.isMember(p.getName()) && GuildManager.isGuild(input) ){ // && !input.equals("dummy")
                        if (GuildManager.addGuildMember(input, p.getName())) {
                            p.sendMessage(input + " 길드에 가입되셨습니다!");
                        }
                    }else if(UserManager.isMember(p.getName())){
                        Optional<UserManager.UserProperty> userProp = UserManager.getUserProperties(p.getName());
                        if(userProp.isPresent()){
                            p.sendMessage("이미 " + userProp.get().guild() + " 길드에 가입되어있습니다.");
                        }
                    }else{
                        p.sendMessage(input + "는 존재하는 길드 이름이 아닙니다.");
                    }
                    GuildInv.open(p);
                });
                //수랑: 길드목록을 공개적으로 전부 보여주고 선택하게 하는게 어떨까
            }//길드 가입
        }

    }
}
