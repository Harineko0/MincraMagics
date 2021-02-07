package jp.mincra.mincramagics.command;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SkillCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Entity) {
            return skill((Player) sender);

        }
        return false;
    }

    private boolean skill(Player sender) {
        sender.openInventory(MincraMagics.getPlayerManager().getMaterialInventory(sender.getUniqueId()));

        return true;
    }
}
