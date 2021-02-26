package jp.mincra.mincramagics.container;

import java.util.UUID;

public class MincraEntity {

    private UUID uuid;
    private String mcr_id;

    public String getMcr_id() {
        return mcr_id;
    }

    public void setMcr_id(String mcr_id) {
        this.mcr_id = mcr_id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
