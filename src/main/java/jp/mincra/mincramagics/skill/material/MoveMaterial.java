package jp.mincra.mincramagics.skill.material;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.player.PlayerUseMaterialEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoveMaterial implements PlayerUseMaterialEvent {

    @Override
    public void onPlayerUseMaterial(Player player, String mcr_id) {

        if (mcr_id.contains("move")) {

            int level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));

            Location location = player.getLocation();

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300 / level , 15 * level));
            player.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.3f, 1f);

            MincraParticle mincraParticle = new MincraParticle();
            mincraParticle.setRadius(2.8);
            mincraParticle.setParticle(Particle.SPELL_INSTANT);
            mincraParticle.drawMagicCircle(location.add(0, 0.25, 0), 5, 1);
        }
    }
}
