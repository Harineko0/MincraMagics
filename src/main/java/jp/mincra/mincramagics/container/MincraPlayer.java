package jp.mincra.mincramagics.container;

import java.util.UUID;

public class MincraPlayer {

    private String playerName = "Unknown";
    private UUID playerUUID;

    private float playerCooltime_value = 0;
    private float playerCooltime_max = 0;
    private String cooltimeTitle;

    private String[] materialList = new String[9];
    private int materialPoint = 100;


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

    public void setMaterial(int slot, String mcr_id) {
        if (slot < 10 && mcr_id.contains("material_")) {
            materialList[slot] = mcr_id;
        }
    }
    public String getMaterial(int slot) {
        if (slot < 10) {
            return materialList[slot];
        }
        return null;
    }

    public void setMaterialPoint(int materialPoint) {
        this.materialPoint = materialPoint;
    }
    public int getMaterialPoint() {
        return materialPoint;
    }
}
