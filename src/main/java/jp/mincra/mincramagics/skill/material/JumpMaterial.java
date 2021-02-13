package jp.mincra.mincramagics.skill.material;

import jp.mincra.mincramagics.event.player.PlayerUseMaterialEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class JumpMaterial implements PlayerUseMaterialEvent {

    @Override
    public void onPlayerUseMaterial(Player player, String mcr_id) {

        if (mcr_id.contains("jump")) {

            int level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));
            Location location = player.getLocation();

            player.setFallDistance(-12F);
            player.setVelocity(player.getVelocity().add(new Vector(0,0.6f * level,0)));
            player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);


            MincraParticle mincraParticle = new MincraParticle();
            mincraParticle.setRadius(2.8);
            mincraParticle.setParticle(Particle.CLOUD);
            mincraParticle.drawMagicCircle(location.add(0, 0.25, 0), 6, 1);

        }
    }
}
