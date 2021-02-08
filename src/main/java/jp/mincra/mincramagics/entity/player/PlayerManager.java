package jp.mincra.mincramagics.entity.player;

import jp.mincra.mincramagics.container.MincraPlayer;
import org.bukkit.Bukkit;
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

    public void setMaterialInventory(UUID uuid, Inventory inventory) {
        MincraPlayerMap.get(uuid).setMaterialInventory(inventory);
    }
    public Inventory getMaterialInventory(UUID uuid) {
        return MincraPlayerMap.get(uuid).getMaterialInventory();
    }

    public void setMaterialPoint(UUID uuid, Integer point) {
        MincraPlayerMap.get(uuid).setMaterialPoint(point);
    }
    public Integer getMaterialPoint(UUID uuid) {
        return MincraPlayerMap.get(uuid).getMaterialPoint();
    }


    //オンラインプレイヤー
    public Player[] getOnlinePlayerList(){
        return onlinePlayerList;
    }
    public void setOnlinePlayerList(){
        onlinePlayerList = Bukkit.getOnlinePlayers().toArray(new Player[0]);
    }
}
