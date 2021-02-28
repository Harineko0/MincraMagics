package jp.mincra.mincramagics.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public abstract class PlayerUseMaterialEvent extends PlayerEvent {

    private String mcrID;

    public PlayerUseMaterialEvent(Player player, String mcrID) {
        super(player);
        this.mcrID = mcrID;
    }

    public String getMcrID() {
        return mcrID;
    }
}
