package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.skill.MincraParticle;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class JumpRod {

    public void JumpOne(Player player) {
        String id = "rod_jump_1";

        if (MincraMagics.getSkillManager().canUseSkill(player, id)) {
            Location location = player.getLocation();

            if (location.getBlockY() < 170) {
                MincraMagics.getSkillManager().useSkill(player, id);

                //メイン
                player.setFallDistance(-5);

                Vector vector = new Vector();
                vector.setY(0.5);

                if (player.getVelocity().getY() > 0) {
                    player.setVelocity(player.getVelocity().add(vector));

                } else {
                    player.setVelocity(player.getVelocity().setY(0.5));

                }

                //装飾
                location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRAVEL, 0.01F, 2);
                location.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.1F, 1);

                MincraParticle mincraParticle = new MincraParticle();
                mincraParticle.setRadius(2.4);
                mincraParticle.setParticle(Particle.REDSTONE);
                mincraParticle.setDustOptions(new Particle.DustOptions(Color.RED, 1));
                mincraParticle.setAngle(0, 0, Math.toRadians(player.getLocation().getYaw()));
                ChatUtil.sendConsoleMessage(String.valueOf(Math.toRadians(player.getLocation().getYaw())));
                mincraParticle.drawMagicCircle(location.add(0, 1.25, 0), 6, 1, 3, 0.05, 0.1);

            } else {
                player.sendMessage(ChatUtil.translateHexColorCodes("&c高すぎるため使えません！"));
            }
        }
    }

    public void JumpTwo(Player player) {
        String id = "rod_jump_2";

        if (MincraMagics.getSkillManager().canUseSkill(player, id)) {
            Location location = player.getLocation();

            if (location.getBlockY() < 170) {
                MincraMagics.getSkillManager().useSkill(player, id);

                //メイン
                player.setFallDistance(-12);

                Vector vector = new Vector();
                vector.setY(1.1);

                if (player.getVelocity().getY() > 0) {
                    player.setVelocity(player.getVelocity().add(vector));

                } else {
                    player.setVelocity(player.getVelocity().setY(1.1));

                }

                //装飾

                location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRAVEL, 0.1F, 2);
                location.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.25F, 1);
                location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5F, 1);
                location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location.add(0, 0.3, 0), 1);

                MincraParticle mincraParticle = new MincraParticle();
                mincraParticle.setRadius(2.4);
                mincraParticle.setYaw(Math.toRadians(player.getLocation().getYaw()));
                mincraParticle.setParticle(Particle.CLOUD);
                mincraParticle.drawMagicCircle(location.add(0, 1.0, 0), 6, 1, 1, 0, 0.01);

            } else {
                player.sendMessage(ChatUtil.translateHexColorCodes("&c高すぎるため使えません！"));
            }
        }
    }

    public void JumpThree(Player player) {
        String id = "rod_jump_3";

        if (MincraMagics.getSkillManager().canUseSkill(player, id)) {
            Location location = player.getLocation();

            if (location.getBlockY() < 170) {
                MincraMagics.getSkillManager().useSkill(player, id);

                //メイン
                player.setFallDistance(-16);

                Vector vector = new Vector();
                vector.setY(1.3);

                if (player.getVelocity().getY() > 0) {
                    player.setVelocity(player.getVelocity().add(vector));

                } else {
                    player.setVelocity(player.getVelocity().setY(0.3));

                }

                //装飾

                location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRAVEL, 0.1F, 2);
                location.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.75F, 1);
                location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5F, 1);
                location.getWorld().spawnParticle(Particle.CLOUD, location.add(0, 0.3, 0), 35, 0.23, 0, 0.23, 1);

                MincraParticle mincraParticle = new MincraParticle();
                mincraParticle.setRadius(2.4);
                mincraParticle.setYaw(Math.toRadians(player.getLocation().getYaw()));
                mincraParticle.setParticle(Particle.CLOUD);
                mincraParticle.drawMagicCircle(location.add(0, 1.25, 0), 6, 1, 1, 0, 0.01);

            } else {
                player.sendMessage(ChatUtil.translateHexColorCodes("&c高すぎるため使えません！"));
            }
        }
    }
}
