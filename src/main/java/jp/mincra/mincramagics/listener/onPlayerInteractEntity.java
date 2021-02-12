package jp.mincra.mincramagics.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class onPlayerInteractEntity implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if(e.getHand() == EquipmentSlot.HAND) {

            final Player player = e.getPlayer();
            final ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);

                if (nbtItem.hasKey("MincraMagics")) {

                    String mcr_id = nbtItem.getCompound("MincraMagics").getString("id");

                    if (mcr_id.contains("rod")) {

                        e.setCancelled(true);

                        if (MincraMagics.getSkillManager().canUseSkill(player, mcr_id)) {
                            MincraMagics.getSkillManager().useSkill(player, mcr_id);

                            //魔法杖イベント実行
                            MincraMagics.getEventNotifier().runPlayerUseMagicRodToEntity(player, e.getRightClicked(), nbtItem.getCompound("MincraMagics").getString("id"));
                        }
                    }
                }
            }
        }
    }
}
