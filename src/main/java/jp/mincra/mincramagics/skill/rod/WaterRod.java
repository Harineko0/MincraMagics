package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Timer;
import java.util.TimerTask;

public class WaterRod implements Listener {

    @EventHandler
    public void onPlayerUseMagicRod(PlayerUseMagicRodEvent event) {
        String mcr_id = event.getMcrID();

        if (mcr_id.contains("rod_water")) {

            Player player = event.getPlayer();

            switch (Integer.parseInt(mcr_id.substring(mcr_id.length() - 1))) {
                case 1:
                    WaterOne(player);
                    break;
            }
        }
    }

    public void WaterOne(Player player) {

        //メイン
        final int[] duration = {600};

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Player finalPlayer = Bukkit.getPlayer(player.getUniqueId());

                Location location = finalPlayer.getLocation();

                if (location.add(0, 1, 0).getBlock().getType().equals(Material.WATER)) {
                    float yaw = location.getYaw();
                    float pitch = location.getPitch();
                    float yswim = (float) (-Math.tan(pitch * Math.PI / 180.0) * 0.6F);

                    if (yswim > 0.6) {
                        yswim = 0.6f;
                    } else if (yswim < -10) {
                        yswim = -10;
                    }

                    Vector vector = new Vector(-1 * Math.sin(yaw * Math.PI / 180.0) * 0.6F, yswim, Math.cos(yaw * Math.PI / 180.0) * 0.6F);
                    finalPlayer.setVelocity(vector);

                    location.getWorld().playSound(location, Sound.BLOCK_WATER_AMBIENT, 0.1F, 10);
                    location.getWorld().spawnParticle(Particle.BUBBLE_POP, location, 2, 0.43, 0.43, 0.43, 10);

                    //削除
                    duration[0] = duration[0] - 1;
                    if (duration[0] < 0) {
                        timer.cancel();
                    }
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 50);


        //装飾
        Location location = player.getLocation();

        location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRAVEL, (float) 0.1, 2);

        MincraParticle mincraParticle = new MincraParticle();
        mincraParticle.setRadius(2.4);
        mincraParticle.setParticle(Particle.BUBBLE_POP);
        mincraParticle.drawMagicCircle(location, 5, 1, 4, 0.01, 0.1);

    }
}
