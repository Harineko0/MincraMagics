package jp.mincra.mincramagics.container;

import java.util.UUID;

public class MincraPlayer {

    private String playerName = "Unknown";
    private UUID playerUUID;

    /*
    private float playerMP_value = 20;
    private float playerMP_max = 20;
     */

    private float playerCooltime_value = 0;
    private float playerCooltime_max = 0;
    private String cooltimeTitle;


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

    /*
    public void setPlayerMP_value(float playerMP_value){
        this.playerMP_value = playerMP_value;
    }
    public float getPlayerMP_value(){
        return playerMP_value;
    }

    public void setPlayerMP_max(float playerMP_max) {
        this.playerMP_max = playerMP_max;
    }
    public float getPlayerMP_max() {return playerMP_max;}
     */

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

}
