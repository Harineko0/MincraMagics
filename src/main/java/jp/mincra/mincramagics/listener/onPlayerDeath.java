package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onPlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        Location deadLocation = MincraMagics.getPlayerManager().getDeadLocation(player.getUniqueId());
        
        if (deadLocation != null) {
            player.teleport(deadLocation);

            player.sendMessage(deadLocation.toString());

        }
    }
}
