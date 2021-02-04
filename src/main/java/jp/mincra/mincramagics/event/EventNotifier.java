package jp.mincra.mincramagics.event;

import jp.mincra.mincramagics.event.entity.CustomEntitySpawnEvent;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodToEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventNotifier {

    private List<PlayerUseMagicRodEvent> playerUseMagicRod = new ArrayList<>();
    private List<PlayerUseMagicRodToEntityEvent> playerUseMagicRodToEntity = new ArrayList<>();
    private List<CustomEntitySpawnEvent> customEntitySpawn = new ArrayList<>();

    /**
     * プレイヤーが魔法杖を使ったときのイベント
     * @param player 使用したプレイヤー
     * @param mcr_id 使用した杖のmcr_id
     */
    public void runPlayerUseMagicRod(Player player, String mcr_id) {
        if (this.playerUseMagicRod != null) {
            for (PlayerUseMagicRodEvent playerUseMagicRodEvent : playerUseMagicRod) {
                playerUseMagicRodEvent.onPlayerUseMagicRod(player, mcr_id);
            }
        }
    }

    /**
     * プレイヤーがエンティティに魔法杖を使ったときのイベント
     * @param player 使用したプレイヤー
     * @param target 実行した対象
     * @param mcr_id 使用した杖のmcr_id
     */
    public void runPlayerUseMagicRodToEntity(Player player, Entity target, String mcr_id) {
        if (this.playerUseMagicRodToEntity != null) {
            for (PlayerUseMagicRodToEntityEvent playerUseMagicRodToEntityEvent : playerUseMagicRodToEntity) {
                playerUseMagicRodToEntityEvent.onPlayerUseMagicRodToEntity(player, target, mcr_id);
            }
        }
    }

    /**
     * カスタムエンティティがスポーンしたときに実行
     * @param entity イベント
     * @param mcr_id スポーンさせるエンティティ名
     */
    public void runCustomEntitySpawn(Entity entity, String mcr_id) {
        if (this.customEntitySpawn != null) {
            for (CustomEntitySpawnEvent customEntitySpawnEvent : customEntitySpawn) {
                customEntitySpawnEvent.onCustomEntitySpawn(entity, mcr_id);
            }
        }
    }

    /**
     * リスナーを追加する。
     * @param listener リスナーを実装したクラス
     */
    public void registerEvents(MincraListener listener) {
        if (listener instanceof PlayerUseMagicRodEvent) {
            playerUseMagicRod.add((PlayerUseMagicRodEvent) listener);
        }
        if (listener instanceof PlayerUseMagicRodToEntityEvent) {
            playerUseMagicRodToEntity.add((PlayerUseMagicRodToEntityEvent) listener);
        }
        if (listener instanceof CustomEntitySpawnEvent) {
            customEntitySpawn.add((CustomEntitySpawnEvent) listener);
        }
    }
}
