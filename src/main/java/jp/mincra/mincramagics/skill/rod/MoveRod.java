package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.util.ChatUtil;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class MoveRod implements Listener {

    @EventHandler
    public void onPlayerUseMagicRod(PlayerUseMagicRodEvent event) {
        String mcr_id = event.getMcrID();

        if (mcr_id.contains("rod_move")) {

            int level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));
            Player player = event.getPlayer();
            Location loc = player.getLocation();


            if (loc.getBlockY() < 170) {

                //メイン
                float yaw = player.getLocation().getYaw();

                Vector vec = new Vector(-level * Math.sin(yaw * Math.PI / 180.0), -0.5, Math.cos(yaw * Math.PI / 180.0) * level);
                player.setVelocity(vec);

                //装飾
                player.playSound(loc, Sound.ENTITY_WITHER_SHOOT, 0.2F, 1F);

                MincraParticle mincraParticle = new MincraParticle();
                mincraParticle.setYaw(Math.toRadians(yaw));
                mincraParticle.setRadius(2.4);
                mincraParticle.setParticle(Particle.SPELL_INSTANT);
                //距離調整
                vec.multiply(level);
                vec.setY(vec.getY() + 1 + 0.25 * level);
                mincraParticle.drawMagicCircle(loc.add(vec), 5, 1);

            } else {
                player.sendMessage(ChatUtil.setColorCodes("&c高すぎるため使えません！"));
            }
        }
    }
}
