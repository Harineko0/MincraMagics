package jp.mincra.mincramagics.command;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.BossBarUtil;
import jp.mincra.mincramagics.util.ChatUtil;
import jp.mincra.mincramagics.util.MobUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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

                        List<Entity> entityList = caster.getNearbyEntities(10,10,10);

                        for (Entity entity : entityList) {
                            MobUtil.damage(entity, 10, Magic);

                        }

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

            if (MincraMagics.getMobManager().isExistEntity(args[1])) {

                JSONObject entityJSONObject = MincraMagics.getMobManager().getEntityJSONObject(args[1]);
                String mcr_id = entityJSONObject.getString("mcr_id");

                Location location = caster.getLocation();
                Entity entity = caster.getWorld().spawnEntity(location, EntityType.valueOf(entityJSONObject.getString("id").toUpperCase()));

                NBTEntity nbtEntity = new NBTEntity(entity);
                nbtEntity.mergeCompound(new NBTContainer(MincraMagics.getMobManager().getMobNBT(mcr_id).toString()));

                StringBuilder builder = new StringBuilder("mcr_");
                builder.append(mcr_id);
                nbtEntity.getStringList("Tags").add(builder.toString());

                MincraMagics.getEventNotifier().runCustomEntitySpawn(entity, mcr_id);

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
