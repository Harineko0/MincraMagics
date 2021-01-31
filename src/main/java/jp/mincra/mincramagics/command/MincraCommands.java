package jp.mincra.mincramagics.command;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.BossBarUtil;
import jp.mincra.mincramagics.util.ChatUtil;
import jp.mincra.mincramagics.skill.MincraParticle;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class MincraCommands implements CommandExecutor {

    private JavaPlugin plugin;
    public MincraCommands(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Entity caster = null;

        if (sender instanceof Entity) caster = (Entity)sender;

        if (args.length < 1){
            //argsが空っぽの時の処理
            ChatUtil.sendMessage(ChatUtil.debug("引数が空です。"),caster);
            return false;
        }

        switch (args[0]) {
            case "reload":
                MincraMagics.reload();
                ChatUtil.sendMessage(ChatUtil.debug("プラグインをリロードします..."),caster);
                return true;

            case "test":
                if (caster instanceof Player) {
                    MincraParticle mincraParticle = new MincraParticle();
                    mincraParticle.setRadius(10);
                    mincraParticle.setParticle(Particle.SPELL_INSTANT);
                    mincraParticle.setRolling(Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));
                    mincraParticle.setRotationAxis(new Vector(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
                    mincraParticle.drawMagicCircle(caster.getLocation(),5, 1,1,0,0);


                    Timer timer = new Timer(); // 今回追加する処理
                    Entity finalCaster = caster;
                    final double[] i = {0};

                    TimerTask task = new TimerTask() {
                            public void run() {
                                i[0] += Double.parseDouble(args[7]);

                                mincraParticle.setAngle(i[0]);
                                mincraParticle.drawMagicCircle(finalCaster.getLocation(),5, 1,1,0,0);

                                if (i[0] > 20) {
                                    timer.cancel();
                                }

                            }
                        };
                    timer.scheduleAtFixedRate(task,0,50);


                    return true;
                }

            case "give":
                return give(caster,args);

            case "cooltime":
                return cooltime(caster,args);

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
