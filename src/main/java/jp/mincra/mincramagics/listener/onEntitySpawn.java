package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class onEntitySpawn implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (MincraMagics.getMobManager().isRandomSpawn(entity.getType()))
            MincraMagics.getMobManager().setEntityRandomNBT(entity);
    }
}