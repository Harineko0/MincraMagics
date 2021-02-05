package jp.mincra.mincramagics.entity.player;

import jp.mincra.mincramagics.container.MincraPlayer;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

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

    /*
    //MP
    public float getPlayerMP_value(UUID uuid) {
        return MincraPlayerMap.get(uuid).getPlayerMP_value();
    }
    public void addPlayerMP_value(UUID uuid, float mp_value) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        mp_value = mp_value + mincraPlayer.getPlayerMP_value();
        mincraPlayer.setPlayerMP_value(mp_value);
        MincraPlayerMap.put(uuid,mincraPlayer);
    }
    public void setPlayerMP_value(UUID uuid, float mp_value) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        mincraPlayer.setPlayerCooltime_max(mp_value);
        MincraPlayerMap.put(uuid,mincraPlayer);
    }

    public float getPlayerMP_max(UUID uuid) {
        return MincraPlayerMap.get(uuid).getPlayerMP_max();
    }
    public void setPlayerMP_max(UUID uuid, float mp_max) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        mincraPlayer.setPlayerCooltime_max(mp_max);
        MincraPlayerMap.put(uuid,mincraPlayer);
    }
    */

    //Cooltime
    public float getPlayerCooltime_value(UUID uuid) {
        return MincraPlayerMap.get(uuid).getPlayerCooltime_value();
    }
    public void addPlayerCooltime_value(UUID uuid, float cooltime_value) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        cooltime_value = cooltime_value + mincraPlayer.getPlayerCooltime_value();
        mincraPlayer.setPlayerCooltime_value(cooltime_value);
        MincraPlayerMap.put(uuid,mincraPlayer);
    }

    public float getPlayerCooltime_max(UUID uuid) {
        return MincraPlayerMap.get(uuid).getPlayerCooltime_max();
    }

    public void setPlayerCooltime_value(UUID uuid, float cooltime) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        mincraPlayer.setPlayerCooltime_value(cooltime);
        MincraPlayerMap.put(uuid,mincraPlayer);
    }

    public void setPlayerCooltime(UUID uuid, float cooltime) {
        setPlayerCooltime_max(uuid,cooltime);
        setPlayerCooltime_value(uuid,cooltime);
    }

    public void setPlayerCooltime_max(UUID uuid, float cooltime) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        mincraPlayer.setPlayerCooltime_max(cooltime);
        MincraPlayerMap.put(uuid,mincraPlayer);
    }

    public void setCooltimeTitle(UUID uuid, String cooltimeTitle) {
        MincraPlayer mincraPlayer = MincraPlayerMap.get(uuid);
        mincraPlayer.setCooltimeTitle(cooltimeTitle);
        MincraPlayerMap.put(uuid,mincraPlayer);
    }
    public String getCooltimeTitle(UUID uuid){
        return MincraPlayerMap.get(uuid).getCooltimeTitle();
    }


    //オンラインプレイヤー
    public Player[] getOnlinePlayerList(){
        return onlinePlayerList;
    }
    public void setOnlinePlayerList(){
        onlinePlayerList = Bukkit.getOnlinePlayers().toArray(new Player[0]);
    }
}
