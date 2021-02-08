package jp.mincra.mincramagics.container;

import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class MincraPlayer {

    private String playerName = "Unknown";
    private UUID playerUUID;

    private float playerCooltime_value = 0;
    private float playerCooltime_max = 0;
    private String cooltimeTitle;

    private int materialPoint = 100;
    private Inventory materialInventory = Bukkit.createInventory(null, 9, new StringBuilder(ChatUtil.setColorCodes("&#2a93b0&f&l[&#2d9ebd&f&lマテリアル&#2a93b0&f&l] &8残りSP: ")).append(materialPoint).toString());

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
        return materialInventory;
    }
    public void setMaterialInventory(Inventory materialInventory) {
        this.materialInventory = materialInventory;
    }

    public void setMaterialPoint(int materialPoint) {
        this.materialPoint = materialPoint;
    }
    public int getMaterialPoint() {
        return materialPoint;
    }
}
