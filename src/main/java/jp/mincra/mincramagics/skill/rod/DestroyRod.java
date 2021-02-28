package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DestroyRod implements Listener {

    private Player caster;
    private Entity target;

    @EventHandler
    private void onPlayerUseMagicRod(PlayerUseMagicRodEvent event) {
        String mcr_id = event.getMcrID();

        if (mcr_id.contains("rod_destroy")) {

            caster = event.getPlayer();
            target = event.getTarget();

                int level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));
                Location location = caster.getLocation();

                //装飾
                location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRAVEL, 0.1F, 2);

                MincraParticle mincraParticle = new MincraParticle();
                mincraParticle.setParticle(Particle.VILLAGER_HAPPY);
                mincraParticle.setRadius(2.4);
                mincraParticle.drawMagicCircle(location, 7, 1);

                new BukkitRunnable() {
                    @Override
                    public void run(){
                        if (level > 3) {
                            List<Entity> entityList = caster.getNearbyEntities(9, 5, 9);
                            entityList.add(caster);

                            for (Entity entity : entityList) {
                                if (entity instanceof Player) {
                                    main(entity, level, mincraParticle);
                                }
                            }
                        } else {
                            if (target != null) {
                                main(target, level, mincraParticle);
                            }
                        }

                    }
                }.runTaskLater(MincraMagics.getInstance(), 5);

        }
    }


    private void main(Entity entity, int level, MincraParticle mincraParticle) {
        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*60*level, level-1));

        if (level == 5) {
            caster.setFoodLevel(caster.getFoodLevel() - 5);
            if (caster.getHealth() > 10) {
                caster.setHealth(caster.getHealth() - 10);
            } else {
                caster.setHealth(1);
            }

            ItemStack itemStack = caster.getInventory().getItemInMainHand();

            itemStack.setDurability((short) (itemStack.getDurability() + 20));

        }

        entity.sendMessage(ChatColor.GOLD + caster.getName() + "から破壊の書lv" + level + "の効果を受けました。");
        caster.sendMessage(ChatColor.GOLD + entity.getName() + "に破壊の書lv" + level + "を使用しました。");

        Location entityLocation = entity.getLocation();
        entity.getWorld().playSound(entityLocation, Sound.BLOCK_ANVIL_USE, 0.2F, 1);

        mincraParticle.drawMagicCircle(entityLocation, 7, 1);
    }
}