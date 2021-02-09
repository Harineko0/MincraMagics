package jp.mincra.mincramagics.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class onInventory implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        /*
        ChatUtil.sendConsoleMessage("clickEvent");
        ChatUtil.sendConsoleMessage(event.getAction().toString());
        ChatUtil.sendConsoleMessage(event.getSlotType().toString());
        ChatUtil.sendConsoleMessage(event.getSlot() + "");
        ChatUtil.sendConsoleMessage(event.getRawSlot() + "");
        ChatUtil.sendConsoleMessage(event.getCursor().toString());
        if (event.getCurrentItem() != null) {
            ChatUtil.sendConsoleMessage("current: " + event.getCurrentItem().toString());
        }

         */

        InventoryView inventoryView = event.getWhoClicked().getOpenInventory();

        if (inventoryView.getTitle().contains("マテリアル")) {

            HumanEntity entity = event.getWhoClicked();
            InventoryAction action = event.getAction();

            switch (action) {
                case PLACE_ALL:
                    if (event.getRawSlot() < 9) {
                        if (!event.getCursor().getType().equals(Material.AIR)) {
                            //マテリアルスロットに入れる
                            NBTItem nbtCursor = new NBTItem(event.getCursor());

                            if (!isMaterial(nbtCursor)) {
                                entity.sendMessage(ChatUtil.setColorCodes("&cマテリアルを選択してください！"));
                                event.setCancelled(true);

                            }
                        }
                    }
                    break;

                case MOVE_TO_OTHER_INVENTORY:
                    if (event.getRawSlot() > 8) {
                        //マテリアルスロットに入れる

                        NBTItem nbtCurrent = new NBTItem(event.getCurrentItem());

                        if (!isMaterial(nbtCurrent)) {
                            entity.sendMessage(ChatUtil.setColorCodes("&cマテリアルを選択してください！"));
                            event.setCancelled(true);

                        }
                    }
                    break;

                case HOTBAR_SWAP:
                    if (event.getRawSlot() < 9) {
                        entity.sendMessage(ChatUtil.setColorCodes("&c数字キーは使用できません！"));
                        event.setCancelled(true);
                    }
                    break;
            }
        }
    }

    private boolean isMaterial(NBTItem nbt) {
        return nbt.hasKey("MincraMagics") && nbt.getCompound("MincraMagics").getString("id").contains("material");
    }

    private void reopenInventory(HumanEntity entity, UUID uuid) {

        new BukkitRunnable() {
            @Override
            public void run() {
                entity.openInventory(MincraMagics.getPlayerManager().getMaterialInventory(uuid));

            }
        }.runTaskLater(MincraMagics.getInstance(), 1);
    }

    private boolean isPickup(InventoryAction action) {
        return action.equals(InventoryAction.PICKUP_ALL) || action.equals(InventoryAction.PICKUP_HALF) || action.equals(InventoryAction.PICKUP_ONE) || action.equals(InventoryAction.PICKUP_SOME);
    }

    private void returnItem(InventoryClickEvent event) {

        UUID uuid = event.getWhoClicked().getUniqueId();
        int skillPoint = MincraMagics.getPlayerManager().getMaterialPoint(uuid);
        Inventory inventory = MincraMagics.getPlayerManager().getMaterialInventory(uuid);
        PlayerInventory playerInventory = event.getWhoClicked().getInventory();

        ChatUtil.sendConsoleMessage(skillPoint+"");

        for (int i=0, len=inventory.getSize(); i<len; i++) {
            ItemStack itemStack = inventory.getItem(i);

            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                NBTItem nbtItem = new NBTItem(itemStack);

                if (nbtItem.hasKey("MincraMagics") && nbtItem.getCompound("MincraMagics").getString("id").contains("material")) {

                     //スキルポイントが正になったら終了
                     if (skillPoint < 0) {
                         skillPoint += MincraMagics.getSkillManager().getSkillPoint(nbtItem.getCompound("MincraMagics").getString("id"));
                         inventory.remove(itemStack);
                         playerInventory.addItem(itemStack);

                         ChatUtil.sendConsoleMessage(String.valueOf(skillPoint));

                     } else {
                         break;

                     }
                }
            }
        }
    }
}
