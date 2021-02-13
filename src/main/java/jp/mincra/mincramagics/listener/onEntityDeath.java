package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.util.MobUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class onEntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        if (entity.getType() != EntityType.PLAYER) {

            if (MobUtil.existEntityNear(entity, EntityType.PLAYER, 10, 10, 10))
                entity.setCustomNameVisible(false);

        }
    }

}
