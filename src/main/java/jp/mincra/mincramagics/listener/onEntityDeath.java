package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class onEntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        UUID uuid = event.getEntity().getUniqueId();

        MincraMagics.getMobManager().removeMincraEntity(uuid);
    }
}
