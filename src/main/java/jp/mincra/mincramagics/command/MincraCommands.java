package jp.mincra.mincramagics.command;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.entity.player.PlayerManager;
import jp.mincra.mincramagics.util.MincraChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
            caster.sendMessage(MincraChatUtil.debug("引数が空です。"));
            return false;
        }

        switch (args[0]){
            case "reload":
                MincraMagics.reload();
                sender.sendMessage(MincraChatUtil.debug("リロード中..."));
                return true;

            case "test":
                if (caster instanceof Player) {
                    PlayerManager playerManager = MincraMagics.getPlayerManager();
                    playerManager.addPlayerMP_value(caster.getUniqueId(), -20);
                    playerManager.setPlayerCooltime(caster.getUniqueId(), 10);

//            ItemStack itemStack = caster.getInventory().getItemInMainHand();
//            NBTItem nbtItem = new NBTItem(itemStack);
//            caster.sendMessage(MincraChatUtil.makeDebugMessage(nbtItem.toString()));
//            NBTCompound nbtCompound = nbtItem.addCompound("MincraMagics");
//            nbtCompound.setString("id","daiyanoken");
//            caster.sendMessage(MincraChatUtil.makeDebugMessage(nbtCompound.toString()));
//            caster.sendMessage(MincraChatUtil.makeDebugMessage(nbtItem.toString()));
//
//            itemStack = nbtItem.getItem();
//            int slot = caster.getInventory().getHeldItemSlot();
//            caster.getInventory().setItem(slot,itemStack);

                    caster.sendMessage(MincraChatUtil.debug(MincraMagics.getJsonManager().getItemNode().get(0).get("mcr_id").toString()));
                    return true;

                }
        }
        return false;
    }
}
