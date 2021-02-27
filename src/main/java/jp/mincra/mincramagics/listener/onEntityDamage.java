package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.MobUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class onEntityDamage implements Listener {

    private Map<DamageCause, MobUtil.DamageType> damageTypeMap = new HashMap<DamageCause, MobUtil.DamageType>() {
        {
            put(DamageCause.ENTITY_ATTACK, MobUtil.DamageType.Physical);
            put(DamageCause.ENTITY_SWEEP_ATTACK, MobUtil.DamageType.Physical);
            put(DamageCause.FALL, MobUtil.DamageType.Fall);
            put(DamageCause.PROJECTILE, MobUtil.DamageType.Projectile);
            put(DamageCause.BLOCK_EXPLOSION, MobUtil.DamageType.Fire);
            put(DamageCause.ENTITY_EXPLOSION, MobUtil.DamageType.Fire);
            put(DamageCause.FIRE, MobUtil.DamageType.Fire);
            put(DamageCause.FIRE_TICK, MobUtil.DamageType.Fire);
            put(DamageCause.HOT_FLOOR, MobUtil.DamageType.Fire);
            put(DamageCause.LAVA, MobUtil.DamageType.Fire);
            put(DamageCause.DROWNING, MobUtil.DamageType.Water);
            put(DamageCause.SUFFOCATION, MobUtil.DamageType.Ice);
            put(DamageCause.LIGHTNING, MobUtil.DamageType.Thunder);
            put(DamageCause.WITHER, MobUtil.DamageType.End);
            put(DamageCause.DRAGON_BREATH, MobUtil.DamageType.End);
        }
    };

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        Entity entity = event.getEntity();

        healthBar(entity);

        calcDamage(event);

    }

    private void calcDamage(EntityDamageEvent event) {

        Entity entity = event.getEntity();
        EntityDamageEvent.DamageCause damageCause = event.getCause();
        MobUtil.DamageType damageType = damageTypeMap.get(damageCause);

        Double DEF = MobUtil.getDEF(entity, damageType);
        if (DEF == null)
            DEF = 1d;
        event.setDamage(DEF * event.getDamage());

    }

    private void healthBar(Entity entity) {
        if (entity.getType() != EntityType.PLAYER) {

            //体力バー
            if (MobUtil.existEntityNear(entity, EntityType.PLAYER, 30, 30, 30)) {
                MobUtil.setHealthBar(entity);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Entity tempEntity = Bukkit.getEntity(entity.getUniqueId());

                        if (tempEntity != null && !(tempEntity instanceof Monster)) {
                            MobUtil.unsetHealthBar(tempEntity);
                        } else if (tempEntity != null && !MobUtil.existEntityNear(tempEntity, EntityType.PLAYER, 10, 10, 10)) {
                            MobUtil.unsetHealthBar(tempEntity);
                        }

                    }
                }.runTaskLater(MincraMagics.getInstance(), 100);
            }
        }
    }
}
