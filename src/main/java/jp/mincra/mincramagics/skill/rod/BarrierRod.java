package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodToEntityEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BarrierRod implements PlayerUseMagicRodEvent,PlayerUseMagicRodToEntityEvent {

    @Override
    public void onPlayerUseMagicRodToEntity(Player player, Entity entity, String mcr_id) {
        if (mcr_id.contains("rod_barrier")) {

            int level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));

            if (level == 1 || level == 2) {
                 //メイン
                Player target = (Player) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, level * 20 * 60, level-1));

                //装飾
                decoration(target.getLocation());

            }
        }
    }


    @Override
    public void onPlayerUseMagicRod(Player caster, String mcr_id) {
        if (mcr_id.contains("rod_barrier")) {

            if (Integer.parseInt(mcr_id.substring(mcr_id.length() - 1)) == 3) {

                if (MincraMagics.getSkillManager().canUseSkill(caster, mcr_id)) {

                    MincraMagics.getSkillManager().useSkill(caster, mcr_id);

                    List<Entity> entityList = caster.getNearbyEntities(9, 5, 9);
                    entityList.add(caster);

                    for (Entity entity : entityList) {
                        //メイン
                        if (entity instanceof Player) {
                            Player target = (Player) entity;
                            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 180, 2));

                            //装飾
                            decoration(target.getLocation());
                        }
                    }

                    //装飾
                    decoration(caster.getLocation());
                }
            }
        }
    }

    private void decoration(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_INFECT, 0.4F, 1);

        MincraParticle mincraParticle = new MincraParticle();
        mincraParticle.setParticle(Particle.SPELL_INSTANT);
        mincraParticle.setRadius(2.4);

        mincraParticle.drawMagicCircle(location.add(0, 0.25, 0), 5, 1);
    }
}
