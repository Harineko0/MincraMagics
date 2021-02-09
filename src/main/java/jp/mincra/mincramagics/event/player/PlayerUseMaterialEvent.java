package jp.mincra.mincramagics.event.player;

import jp.mincra.mincramagics.event.MincraListener;
import org.bukkit.entity.Player;

import java.util.EventListener;

public interface PlayerUseMaterialEvent extends EventListener, MincraListener {

    void onPlayerUseMaterial(Player player, String mcr_id);
}
