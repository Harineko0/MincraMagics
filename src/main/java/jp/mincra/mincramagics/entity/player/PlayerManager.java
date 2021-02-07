package jp.mincra.mincramagics.entity.player;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.container.MincraPlayer;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private ConcurrentHashMap<UUID, MincraPlayer> MincraPlayerMap = new ConcurrentHashMap<>();
    private Player[] onlinePlayerList;

    public ConcurrentHashMap<UUID, MincraPlayer> getMincraPlayerMap() {
        return MincraPlayerMap;
    }
    public void putMincraPlayer(MincraPlayer mincraPlayer) {
        MincraPlayerMap.put(mincraPlayer.getPlayerUUID(),mincraPlayer);
    }
    public void removeMincraPlayer(UUID uuid) {
        MincraPlayerMap.remove(uuid);
    }

    //Cooltime
    public float getPlayerCooltime_value(UUID uuid) {
        return MincraPlayerMap.get(uuid).getPlayerCooltime_value();
    }
    public void addPlayerCooltime_value(UUID uuid, float cooltime_value) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        cooltime_value = cooltime_value + mincraPlayer.getPlayerCooltime_value();
        mincraPlayer.setPlayerCooltime_value(cooltime_value);
        MincraPlayerMap.put(uuid, mincraPlayer);
    }

    public float getPlayerCooltime_max(UUID uuid) {
        return MincraPlayerMap.get(uuid).getPlayerCooltime_max();
    }

    public void setPlayerCooltime_value(UUID uuid, float cooltime) {
        MincraPlayerMap.get(uuid).setPlayerCooltime_value(cooltime);
    }

    public void setPlayerCooltime(UUID uuid, float cooltime) {
        setPlayerCooltime_max(uuid,cooltime);
        setPlayerCooltime_value(uuid, cooltime);
    }

    public void setPlayerCooltime_max(UUID uuid, float cooltime) {
        MincraPlayerMap.get(uuid).setPlayerCooltime_max(cooltime);
    }

    public void setCooltimeTitle(UUID uuid, String cooltimeTitle) {
        MincraPlayerMap.get(uuid).setCooltimeTitle(cooltimeTitle);
    }
    public String getCooltimeTitle(UUID uuid){
        return MincraPlayerMap.get(uuid).getCooltimeTitle();
    }

    //マテリアル
    public void setMaterial(UUID uuid, int slot, String mcr_id) {
        MincraPlayerMap.get(uuid).setMaterial(slot, mcr_id);
    }
    public String getMaterial(UUID uuid, int slot) {
        return MincraPlayerMap.get(uuid).getMaterial(slot);
    }

    public void setMaterialPoint(UUID uuid, Integer point) {
        MincraPlayerMap.get(uuid).setMaterialPoint(point);
    }
    public Integer getMaterialPoint(UUID uuid) {
        return MincraPlayerMap.get(uuid).getMaterialPoint();
    }

    public Inventory getMaterialInventory(UUID uuid) {

        StringBuilder builder = new StringBuilder(ChatUtil.setColorCodes("&#2d9ebd&f&lマテリアル &8残りスキルポイント: "));
        builder.append(getMaterialPoint(uuid));
        Inventory inventory = Bukkit.createInventory(null, 9, builder.toString());

        for (int i=0; i<9; i++) {
            String mcr_id = getMaterial(uuid, i);
            if (mcr_id != null) {
                inventory.addItem(MincraMagics.getItemManager().getItem(mcr_id));
            }
        }

        return inventory;
    }


    //オンラインプレイヤー
    public Player[] getOnlinePlayerList(){
        return onlinePlayerList;
    }
    public void setOnlinePlayerList(){
        onlinePlayerList = Bukkit.getOnlinePlayers().toArray(new Player[0]);
    }
}
