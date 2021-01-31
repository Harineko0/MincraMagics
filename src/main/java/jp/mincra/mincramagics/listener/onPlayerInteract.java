package jp.mincra.mincramagics.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class onPlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //二度実行されるのを防止
        //https://www.spigotmc.org/threads/playerinteractevent-being-called-twice-with-tools.418345/
        if(e.getHand() == EquipmentSlot.HAND) {

            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                final Player player = e.getPlayer();
                final ItemStack item = player.getInventory().getItemInMainHand();

                if (item.getType() != Material.AIR) {
                    NBTItem nbtItem = new NBTItem(item);

                    if (nbtItem.hasKey("MincraMagics")) {
                        e.setCancelled(true);

                        if (nbtItem.getCompound("MincraMagics").getString("id").contains("rod")) {
                            //魔法杖イベント実行
                            MincraMagics.getEventNotifier().runPlayerUseMagicRod(player, nbtItem.getCompound("MincraMagics").getString("id"));
                        }
                    }
                }
            }
        }
    }
}
