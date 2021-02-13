package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.MobUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onEntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        Entity entity = event.getEntity();
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        healthBar(entity);

        //ダメージ計算
        if (damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            //物理
            Double physicalDEF = MobUtil.getPhysicalDEF(entity);
            if (physicalDEF == null)
                physicalDEF = 1d;
            event.setDamage(physicalDEF * event.getDamage());
        }

    }

    private void healthBar(Entity entity) {
        if (entity.getType() != EntityType.PLAYER) {

            //体力バー
            if (MobUtil.existEntityNear(entity, EntityType.PLAYER, 30, 30, 30))
                MobUtil.setHealthBar((LivingEntity) entity);

            //味方モブ・中立モブ用
            if (entity instanceof Monster) {
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MobUtil.unsetHealthBar((LivingEntity) entity);

                    }
                }.runTaskLater(MincraMagics.getInstance(), 100);
            }
        }
    }
}
