package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodToEntityEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DestroyRod implements PlayerUseMagicRodEvent, PlayerUseMagicRodToEntityEvent {

    private Player player;
    private Entity target;
    private String mcr_id;

    @Override
    public void onPlayerUseMagicRodToEntity(Player player, Entity target, String mcr_id) {
        this.player = player;
        this.target = target;
        this.mcr_id = mcr_id;

        run();
    }

    @Override
    public void onPlayerUseMagicRod(Player player, String mcr_id) {
        this.player = player;
        this.mcr_id = mcr_id;

        run();
    }


    private void run() {
        if (mcr_id.contains("rod_destroy")) {

                int level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));
                Location location = player.getLocation();

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
                            List<Entity> entityList = player.getNearbyEntities(9, 5, 9);
                            entityList.add(player);

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
            player.setFoodLevel(player.getFoodLevel() - 5);
            if (player.getHealth() > 10) {
                player.setHealth(player.getHealth() - 10);
            } else {
                player.setHealth(1);
            }

            ItemStack itemStack = player.getInventory().getItemInMainHand();

            itemStack.setDurability((short) (itemStack.getDurability() + 20));

        }

        entity.sendMessage(ChatColor.GOLD + player.getName() + "から破壊の書lv" + level + "の効果を受けました。");
        player.sendMessage(ChatColor.GOLD + entity.getName() + "に破壊の書lv" + level + "を使用しました。");

        Location entityLocation = entity.getLocation();
        entity.getWorld().playSound(entityLocation, Sound.BLOCK_ANVIL_USE, 0.2F, 1);

        mincraParticle.drawMagicCircle(entityLocation, 7, 1);
    }
}