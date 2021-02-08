package jp.mincra.mincramagics.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

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

            switch (event.getAction()) {

                case PLACE_ALL:
                    if (event.getRawSlot() < 9 && !event.getCursor().getType().equals(Material.AIR)) {

                        NBTItem nbtCursor = new NBTItem(event.getCursor());

                        if (!nbtCursor.hasKey("MincraMagics") || (nbtCursor.hasKey("MincraMagics") && !nbtCursor.getCompound("MincraMagics").getString("id").contains("material"))) {
                            event.getWhoClicked().sendMessage(ChatUtil.setColorCodes("&cマテリアルを選択してください！"));
                            event.setCancelled(true);
                        }
                    }
                    break;

                case MOVE_TO_OTHER_INVENTORY:
                    if (event.getRawSlot() > 8) {

                        NBTItem nbtCurrent = new NBTItem(event.getCurrentItem());
                        ChatUtil.sendConsoleMessage("test");

                        if (!nbtCurrent.hasKey("MincraMagics") || (nbtCurrent.hasKey("MincraMagics") && !nbtCurrent.getCompound("MincraMagics").getString("id").contains("material"))) {
                            event.getWhoClicked().sendMessage(ChatUtil.setColorCodes("&cマテリアルを選択してください！"));
                            event.setCancelled(true);
                        }
                    }
                    break;

                case HOTBAR_SWAP:
                    if (event.getRawSlot() < 9){
                        event.getWhoClicked().sendMessage(ChatUtil.setColorCodes("&c数字キーは使用できません！"));
                        event.setCancelled(true);
                    }
                    break;

            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        /*

        HumanEntity humanEntity =  event.getPlayer();
        InventoryView inventoryView = humanEntity.getOpenInventory();

        if (inventoryView.getTitle().contains("マテリアル")) {

            for (int i=0, len=inventoryView.countSlots(); i<len; i++) {

                if (inventoryView.getItem(i) != null) {
                    NBTItem nbt = new NBTItem(inventoryView.getItem(i));

                    if (!nbt.hasKey("MincraMagics") || !nbt.getCompound("MincraMagics").getString("id").contains("material")) {
                        humanEntity.sendMessage(ChatUtil.setColorCodes("&bマテリアル以外のアイテムが入っています！"));
                        humanEntity.openInventory(MincraMagics.getPlayerManager().getMaterialInventory(humanEntity.getUniqueId()));

                    }
                }
            }
        }

         */
    }
}
