package jp.mincra.mincramagics.entity.mob.hostile;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.entity.CustomEntitySpawnEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class BeastRodMob implements CustomEntitySpawnEvent {

    @Override
    public void onCustomEntitySpawn(Entity entity, String mcr_id) {

        if (mcr_id.equals("rod_beast")) {

            new BukkitRunnable() {

                @Override
                public void run() {

                    MincraParticle mincraParticle = new MincraParticle();
                    mincraParticle.setRadius(2.4);
                    mincraParticle.setParticle(Particle.SPELL_INSTANT);

                    mincraParticle.drawMagicCircle(entity.getLocation(), 5, 1);
                    entity.remove();

                    this.cancel();

                }
            }.runTaskLater(MincraMagics.getInstance(), 400);
        }
    }
}
