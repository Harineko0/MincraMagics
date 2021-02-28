package jp.mincra.mincramagics.command;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.BossBarUtil;
import jp.mincra.mincramagics.util.ChatUtil;
import jp.mincra.mincramagics.util.MobUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static jp.mincra.mincramagics.util.MobUtil.DamageType.Magic;

public class MincraCommands implements CommandExecutor {

    private JavaPlugin plugin;
    public MincraCommands(JavaPlugin plugin){
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Entity) {
            Entity caster = (Entity) sender;

            if (args.length < 1) {
                //argsが空っぽの時の処理
                ChatUtil.sendMessage(ChatUtil.debug("引数が空です。"), caster);
                return false;
            }

            switch (args[0]) {
                case "test":
                    if (caster instanceof Player) {

                        Entity creeper = caster.getWorld().spawnEntity(caster.getLocation(), EntityType.CREEPER);

                        PacketContainer packet = MincraMagics.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);

                        packet.getIntegers().write(0, creeper.getEntityId());

                        try {
                            MincraMagics.getProtocolManager().sendServerPacket((Player) caster, packet);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        creeper.remove();

                        MincraMagics.getPlayerManager().setDeadLocation(caster.getUniqueId(), caster.getLocation());

                        ((Player) caster).setHealth(0);
                    }
                    return true;

                case "test2":
                    if (caster instanceof Player) {

                        Player player = (Player) caster;
                        player.setGameMode(GameMode.SPECTATOR);

                        Entity pig = player.getWorld().spawnEntity(player.getLocation(),EntityType.PIG);
                        player.setSpectatorTarget(pig);
                        pig.remove();
//
//                        List<Entity> entityList = caster.getNearbyEntities(10,10,10);
//
//                        for (Entity entity : entityList) {
//                            MobUtil.damage(entity, 10, Magic);
//
//                        }

                        return true;
                    }

                case "reload":
                        MincraMagics.reload();
                        ChatUtil.sendMessage(ChatUtil.debug("プラグインをリロードします..."), caster);
                        return true;

                case "give":
                    return give(caster, args);

                case "cooltime":
                    return cooltime(caster, args);

                case "summon":
                    return summon(caster, args);

            }
        }
        return false;
    }

    private boolean give(Entity caster, @NotNull String[] args) {

        Player player = Bukkit.getPlayer(args[1]);

            ItemStack itemStack = MincraMagics.getItemManager().getItem(args[2]);

            if (args.length > 3) {
                if (StringUtils.isNumeric(args[3])) {
                    itemStack.setAmount(Integer.parseInt(args[3]));

                } else {
                    ChatUtil.sendMessage(ChatUtil.debug("第三引数に整数が必要です。\n"), caster);
                    sendErrorMessage(caster, args, 3);
                    return true;

                }
            }


            if (player != null) {

                if (MincraMagics.getItemManager().getItem(args[2]) != null) {
                    player.getInventory().addItem(itemStack);
                    ChatUtil.sendMessage(ChatUtil.debug(player.getName() + "に" + args[2] + "を付与しました。"),caster);

                } else {
                    ChatUtil.sendMessage(ChatUtil.debug(args[2] + "は未登録のアイテムです。"),caster);
                }
            } else {
                ChatUtil.sendMessage(ChatUtil.debug(args[1] + "は存在しません。"),caster);

            }

        return true;

    }

    private boolean cooltime(Entity caster, @NotNull String[] args) {
        switch (args[1]) {
            case "set":
                Player player = Bukkit.getPlayer(args[2]);
                if (player != null) {
                    BossBarUtil.setCooltimeBossBar(player,"", Float.parseFloat(args[3]),true);
                    ChatUtil.sendMessage(ChatUtil.debug(args[2]+"のクールタイムを"+args[3]+"にセットしました。"),caster);
                } else  {
                    ChatUtil.sendMessage(ChatUtil.debug(args[2]+"は存在しません。"),caster);
                }
                return true;
        }
        return false;
    }

    private boolean summon(Entity caster, @NotNull String[] args) {
        if (caster instanceof Player) {

            if (MincraMagics.getMobManager().isCustomEntity(args[1])) {

                Location location = caster.getLocation();

                Entity entity = caster.getWorld().spawnEntity(location, MincraMagics.getMobManager().getEntityType(args[1]));

                MincraMagics.getMobManager().setEntityNBT(entity, args[1]);

//                MincraMagics.getEventNotifier().runCustomEntitySpawn(entity, args[1]);

                caster.sendMessage(ChatUtil.debug(args[1] + "を召喚しました。"));

            } else {
                StringBuilder builder = new StringBuilder(args[1]);
                builder.append("は存在しません。");

                caster.sendMessage(builder.toString());
            }
        } else {

            ChatUtil.sendConsoleMessage("/summonはプレイヤーのみ実行可能です。");
        }

        return true;
    }

    private void sendErrorMessage(Entity caster, String[] args, int errorArg){

        String finalArgs = "";
        for (int i=0; i<=errorArg; i++) {
            StringBuffer buffer = new StringBuffer(finalArgs);
            buffer.append(args[i]);
            buffer.append(" ");
            finalArgs = buffer.toString();
        }

        if (caster instanceof Player) {
            ChatUtil.sendMessage(ChatUtil.setColorCodes("&7" + finalArgs + "&c←[問題個所]"),caster);
        }

    }
}
