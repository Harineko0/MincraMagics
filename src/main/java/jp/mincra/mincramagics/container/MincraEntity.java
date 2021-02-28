package jp.mincra.mincramagics.container;

import java.util.UUID;

public class MincraEntity {

    private UUID uuid;
    private String mcrID;

    public MincraEntity(UUID uuid, String mcrID) {
        this.uuid = uuid;
        this.mcrID = mcrID;
    }

    public String getMcrID() {
        return mcrID;
    }

    public void setMcrID(String mcrID) {
        this.mcrID = mcrID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
