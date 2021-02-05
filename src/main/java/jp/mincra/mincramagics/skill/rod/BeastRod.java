package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.util.MincraParticle;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeastRod implements PlayerUseMagicRodEvent {

    private MincraParticle mincraParticle = new MincraParticle();

    @Override
    public void onPlayerUseMagicRod(Player player, String mcr_id) {

        if (mcr_id.contains("rod_beast")) {

            if (MincraMagics.getSkillManager().canUseSkill(player, mcr_id)) {

                MincraMagics.getSkillManager().useSkill(player, mcr_id);

                int level = Integer.parseInt(mcr_id.substring(mcr_id.length() - 1));

                Location location = player.getLocation();

                List<ChatColor> chatColorList = new ArrayList<>(Arrays.asList(ChatColor.GREEN, ChatColor.BLUE, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED));
                List<DyeColor> dyeColorList = new ArrayList<>(Arrays.asList(DyeColor.GREEN, DyeColor.BLUE, DyeColor.YELLOW, DyeColor.ORANGE, DyeColor.RED));
                List<Wolf> wolfList = new ArrayList<>(5);

                MincraParticle mincraParticle = new MincraParticle();
                mincraParticle.setRadius(2.4);
                mincraParticle.setParticle(Particle.SPELL_INSTANT);
                mincraParticle.drawMagicCircle(location, 5, 1);

                location.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, (float) 0.1, 2);

                for (int i = 0, amount = level * 2 - 1; i < amount; i++) {
                    Wolf wolf = (Wolf) player.getWorld().spawnEntity(location, EntityType.WOLF);
                    wolf.setCustomName(chatColorList.get(i) + "幻獣");
                    wolf.setAdult();
                    wolf.setOwner(player);
                    wolf.setBreed(false);
                    wolf.setCollarColor(dyeColorList.get(i));
                    wolf.addScoreboardTag("mcr_rod_beast");

                    mincraParticle.drawMagicCircle(wolf.getLocation(), 5, 1);

                    wolfList.add(wolf);

                }

                new BukkitRunnable() {
                    List<Wolf> finalWolfList = wolfList;

                    @Override
                    public void run() {

                        for (Wolf wolf : finalWolfList) {

                            mincraParticle.drawMagicCircle(wolf.getLocation(), 5, 1);
                            wolf.remove();

                            this.cancel();
                        }
                    }
                }.runTaskLater(MincraMagics.getInstance(), level * 200);
            }
        }
    }

    public void despawnWolf(Wolf wolf, long delay) {
        new BukkitRunnable() {

            @Override
            public void run() {
                mincraParticle.drawMagicCircle(wolf.getLocation(), 5, 1);
                wolf.remove();

                this.cancel();
            }
        }.runTaskLater(MincraMagics.getInstance(), delay);
    }
}
