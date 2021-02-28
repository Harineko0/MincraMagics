package jp.mincra.mincramagics.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public abstract class PlayerUseMagicRodEvent extends PlayerEvent {

    private Entity target;
    private String mcrID;

    public PlayerUseMagicRodEvent(Player player, String mcrID) {
        super(player);
        this.mcrID = mcrID;
    }

    public PlayerUseMagicRodEvent(Player player, Entity target, String mcrID) {
        super(player);
        this.target = target;
        this.mcrID = mcrID;
    }

    public Entity getTarget() {
        return target;
    }

    public String getMcrID() {
        return mcrID;
    }
}
