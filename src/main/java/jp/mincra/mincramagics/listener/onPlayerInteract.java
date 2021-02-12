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

import java.util.UUID;

public class onPlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //二度実行されるのを防止
        //https://www.spigotmc.org/threads/playerinteractevent-being-called-twice-with-tools.418345/
        if(e.getHand() == EquipmentSlot.HAND) {

            //杖
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

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
                                MincraMagics.getEventNotifier().runPlayerUseMagicRod(player, nbtItem.getCompound("MincraMagics").getString("id"));

                            }
                        }
                    }
                }
            } else

            //マテリアル
            if ( (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) ) && e.getPlayer().isSneaking() ) {

                Player player = e.getPlayer();
                UUID uuid = player.getUniqueId();
                int slot = e.getPlayer().getInventory().getHeldItemSlot();

                ItemStack item = MincraMagics.getPlayerManager().getMaterialInventory(uuid).getItem(slot);

                if (item != null && !item.getType().equals(Material.AIR)) {
                    NBTItem nbtItem = new NBTItem(item);

                    if (nbtItem.hasKey("MincraMagics")) {

                        String mcr_id =  nbtItem.getCompound("MincraMagics").getString("id");

                        if (mcr_id.contains("material")) {

                            e.setCancelled(true);

                            if (MincraMagics.getSkillManager().canUseSkill(player, mcr_id)) {

                                MincraMagics.getSkillManager().useSkill(player, mcr_id);

                                MincraMagics.getEventNotifier().runPlayerUseMaterial(player, mcr_id);

                            }
                        }
                    }
                }
            }
        }
    }
}
