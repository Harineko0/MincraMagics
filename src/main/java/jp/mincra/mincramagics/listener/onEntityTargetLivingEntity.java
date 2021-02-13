package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.util.MobUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class onEntityTargetLivingEntity implements Listener {

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {

        Entity caster = event.getEntity();
        Entity target = event.getTarget();

        //エンティティがプレイヤーにターゲットしたとき
        if (target != null) {
            if (!caster.getType().equals(EntityType.PLAYER) && target.getType().equals(EntityType.PLAYER)) {
                if (caster instanceof LivingEntity) {
                    MobUtil.setHealthBar((LivingEntity) caster);
                }
            }
        } else {
            if (!caster.getType().equals(EntityType.PLAYER)) {

                MobUtil.unsetHealthBar(caster);
            }

        }
    }
}
