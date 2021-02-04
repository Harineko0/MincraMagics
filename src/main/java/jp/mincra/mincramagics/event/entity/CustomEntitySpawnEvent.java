package jp.mincra.mincramagics.event.entity;

import jp.mincra.mincramagics.event.MincraListener;
import org.bukkit.entity.Entity;

import java.util.EventListener;

public interface CustomEntitySpawnEvent extends EventListener, MincraListener {

    /**
     * カスタムエンティティがスポーンしたときに実行
     * @param entity エンティティ
     * @param mcr_id スポーンさせるエンティティ名
     */
    void onCustomEntitySpawn(Entity entity, String mcr_id);
}
