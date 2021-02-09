package jp.mincra.mincramagics.container;

import de.tr7zw.changeme.nbtapi.NBTItem;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MincraPlayer {

    private String playerName = "Unknown";
    private UUID playerUUID;

    private float playerCooltime_value = 0;
    private float playerCooltime_max = 0;
    private String cooltimeTitle;

    private int materialPoint = 100;
    private Inventory materialInventory = Bukkit.createInventory(null, 9, new StringBuilder(ChatUtil.setColorCodes("&#2a93b0&f[&#2d9ebd&f&lマテリアル&#2a93b0&f] &8残りSP: ")).append(materialPoint).toString());
    private String finalMaterialInventoryTitle;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerUUID(UUID playerUUID){
        this.playerUUID = playerUUID;
    }
    public UUID getPlayerUUID(){
        return playerUUID;
    }

    public void setPlayerCooltime_value(float playerCooltime_value) {
        this.playerCooltime_value = playerCooltime_value;
    }
    public float getPlayerCooltime_value() {
        return playerCooltime_value;
    }

    public void setPlayerCooltime_max(float playerCooltime_max) {
        this.playerCooltime_max = playerCooltime_max;
    }
    public float getPlayerCooltime_max() {return playerCooltime_max;}

    public void setCooltimeTitle(String cooltimeTitle){this.cooltimeTitle = cooltimeTitle;}
    public String getCooltimeTitle(){return cooltimeTitle;}


    public Inventory getMaterialInventory() {

        Inventory inventory = Bukkit.createInventory(materialInventory.getHolder(), materialInventory.getSize(), ChatUtil.setColorCodes(getMaterialInventoryTitle()));

        inventory.setContents(materialInventory.getContents());

        materialInventory = inventory;

        return materialInventory;
    }
    public void setMaterialInventory(Inventory materialInventory) {
        this.materialInventory = materialInventory;
    }

    public void setMaterialPoint() {

        int materialPoint = 100;

        for (int i=0, len=materialInventory.getSize(); i<len; i++) {
            ItemStack item = materialInventory.getItem(i);

            if (item != null) {
                NBTItem nbt = new NBTItem(item);

                if (nbt.hasKey("MincraMagics")) {
                    materialPoint -= MincraMagics.getSkillManager().getSkillPoint(nbt.getCompound("MincraMagics").getString("id"));

                }
            }
        }

        this.materialPoint = materialPoint;
    }

    public int getMaterialPoint() {
        setMaterialPoint();
        return materialPoint;
    }

    public String getMaterialInventoryTitle() {
        String materialInventoryTitle = "&#2a93b0&f[&#2d9ebd&f&lマテリアル&#2a93b0&f] &8残りSP: &#2a93b0&f";
        StringBuilder builder = new StringBuilder(materialInventoryTitle);
        builder.append(materialPoint);
        finalMaterialInventoryTitle = builder.toString();

        return finalMaterialInventoryTitle;
    }
}
