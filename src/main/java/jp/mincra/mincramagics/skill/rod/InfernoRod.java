package jp.mincra.mincramagics.skill.rod;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;


public class InfernoRod implements Listener {

    Player player;
    int level;

    @EventHandler
    public void onPlayerUseMagicRod(PlayerUseMagicRodEvent event) {

        String mcr_id = event.getMcrID();

        if (mcr_id.contains("rod_inferno")) {

                //メイン
                this.player = event.getPlayer();
                level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));

                BukkitTask[] task = new BukkitTask[level * 7];

                for (int i = 0; i < level * 7; i++) {
                    task[i] = new spawnFireball().runTaskLater(MincraMagics.getInstance(), i * (6 - level));
                }

                if (level == 3) {
                    List<Entity> entityList = player.getNearbyEntities(25, 5, 25);

                    List<String> friendlyMobs = MincraMagics.getMobManager().getFriendlyMobs();
                    for (Entity entity : entityList) {
                        if (!friendlyMobs.contains(entity.getType().toString())) {
                            entity.setFireTicks(1250);
                            entity.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, entity.getLocation(), 1, 0.35, 0.35, 0.35, 35);

                        }
                    }
                }

                //装飾
                Location location = player.getLocation();

                location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRAVEL, (float) 0.1, 2);

                MincraParticle mincraParticle = new MincraParticle();
                mincraParticle.setParticle(Particle.FLAME);
                mincraParticle.setRadius(2.4);
                mincraParticle.drawMagicCircle(location.add(0, 0.25, 0), 6, 1, 3, 0.01, 0.05);
        }
    }


    public class spawnFireball extends BukkitRunnable {

        @Override
        public void run() {
            Location location = player.getLocation();
            Random random = new Random();
            if (level == 1) {
                Fireball fireball = location.getWorld().spawn(location.add(0, 10 + Math.sin(random.nextInt(360)) * 4, 0), Fireball.class);
                fireball.setShooter(player);

            } else {
                Fireball fireball = location.getWorld().spawn(location.add(random.nextInt(7) - 3, 10 + Math.sin(random.nextInt(360)) * 4, random.nextInt(7) - 3), Fireball.class);
                fireball.setVelocity(fireball.getVelocity().multiply(level / 1.5));

                NBTEntity nbtFire = new NBTEntity(fireball);
                nbtFire.setInteger("ExplosionPower",level);

                fireball.setShooter(player);

            }


            this.cancel();
        }
    }
}