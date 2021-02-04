package jp.mincra.mincramagics.entity.mob.hostile;

import jp.mincra.mincramagics.event.entity.CustomEntitySpawnEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Timer;
import java.util.TimerTask;

public class ExampleZombieMob implements CustomEntitySpawnEvent {

    @Override
    public void onCustomEntitySpawn(Entity entity, String mcr_id) {

        if (mcr_id.equals("example_zombie")) {

            Zombie zombie = (Zombie) entity;

            MincraParticle mincraParticle = new MincraParticle();
            mincraParticle.setParticle(Particle.VILLAGER_HAPPY);
            mincraParticle.setRadius(4);

            final double[] i = {0};

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    if (zombie.isValid()) {

                        i[0] += 0.1;

                        mincraParticle.setYaw(i[0]);
                        mincraParticle.drawMagicCircle(zombie.getLocation(), 5, 1);

                    } else if (zombie.isDead()) {
                        this.cancel();
                    }
                }
            };
            timer.scheduleAtFixedRate(task,0,50);
        }
    }
}
