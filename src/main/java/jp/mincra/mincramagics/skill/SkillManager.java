package jp.mincra.mincramagics.skill;

import de.tr7zw.changeme.nbtapi.NBTItem;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.container.MincraSkill;
import jp.mincra.mincramagics.util.BossBarUtil;
import jp.mincra.mincramagics.util.ChatUtil;
import jp.mincra.mincramagics.util.ExpUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SkillManager {

    private Map<String, MincraSkill> skillMap = new HashMap<>();
    private int amount;

    public void register(Map<String, JSONArray> jsonArrayMap){
        skillMap = new HashMap<>();
        amount = 0;

        jsonArrayMap.forEach(this::registerSkill);

        ChatUtil.sendConsoleMessage(amount+"個のスキルを登録しました。");
    }

    public void registerSkill(String path, JSONArray jsonArray) {

        ChatUtil.sendConsoleMessage("スキルの登録を開始します... from: " + path);
        JSONObject skillObject;

        for (int i=0, len=jsonArray.length(); i<len; i++) {
            MincraSkill mincraSkill = new MincraSkill();

            skillObject = jsonArray.getJSONObject(i);

            //id
            if (skillObject.has("id") && skillObject.get("id") instanceof String) {
                mincraSkill.setId(skillObject.getString("id"));
                //name
                mincraSkill.setName(skillObject.getString("name"));
                //lore
                mincraSkill.setLore(skillObject.getString("lore"));
                //cooltime
                mincraSkill.setCooltime(skillObject.getFloat("cooltime"));
                //exp_lv
                mincraSkill.setExp_lv(skillObject.getInt("exp_lv"));
                //exp
                mincraSkill.setExp(skillObject.getInt("exp"));
                //break_rate
                mincraSkill.setBreak_rate(skillObject.getFloat("break_rate"));

                amount = amount + 1;
            }
            skillMap.put(skillObject.getString("id"), mincraSkill);
        }
    }

    public Map<String, MincraSkill> getSkillMap(){
        return skillMap;
    }

    public void useSkill(Player player, String id) {

        if (skillMap.keySet().contains(id)) {
            MincraSkill mincraSkill = skillMap.get(id);

            //クールタイム
            BossBarUtil.setCooltimeBossBar(player,mincraSkill.getName(),mincraSkill.getCooltime(),true);

            //MP(経験値
            player.giveExpLevels(-mincraSkill.getExp_lv());
            player.giveExp(-mincraSkill.getExp());

            //崩壊
            Random random = new Random();
            int randomValue = random.nextInt(100);

            if (randomValue < mincraSkill.getBreak_rate() * 100) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    player.getInventory().setItemInMainHand(item);
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }

                NBTItem nbtItem = new NBTItem(item);

                if (mincraSkill.getName().contains("杖")) {
                    player.sendMessage(ChatUtil.setColorCodes("&#f03c3c&f&l杖がっ・・・"));
                } else if (mincraSkill.getName().contains("書")) {
                    player.sendMessage(ChatUtil.setColorCodes("&#f03c3c&f&l書がっ・・・"));
                }

                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK,1f,1f);
            }
        } else {
            ChatUtil.sendConsoleMessage("エラー: "+id+"のスキルが存在しません。");
        }
    }

    public boolean canUseSkill(Player player, String id) {
        boolean canUse = true;

        if (skillMap.keySet().contains(id)) {
            //クールタイム
            if (MincraMagics.getPlayerManager().getPlayerCooltime_value(player.getUniqueId()) > 0) {
                canUse = false;
            }
            //経験値
            if (player.getGameMode() != GameMode.CREATIVE) {
                if (player.getLevel() < skillMap.get(id).getExp_lv()) {
                    player.sendMessage(ChatUtil.setColorCodes("&#adadad&fうわっ・・・私のMP、低すぎ・・・？"));
                    canUse = false;
                }
                if (ExpUtil.getPlayerExp(player) < skillMap.get(id).getExp()) {
                    player.sendMessage(ChatUtil.setColorCodes("&#adadad&fうわっ・・・私のMP、低すぎ・・・？"));
                    canUse = false;
                }
            }

        } else {
            ChatUtil.sendConsoleMessage("エラー: "+id+"のスキルが存在しません。");
            canUse = false;
        }

        return canUse;
    }
}
