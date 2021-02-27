package jp.mincra.mincramagics.util;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTList;
import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.json.JSONObject;

public class MobUtil {

    /**
     * DEFで軽減できるダメージの種類です。
     */
    public enum DamageType {
        Physical,
        Fall,
        Projectile,
        Magic,
        Fire,
        Water,
        Ice,
        Thunder,
        Heaven,
        End
    }

    /**
     * エンティティの防御力を計算してダメージを与えます
     * @param entity 対象のエンティティ
     * @param amount ダメージ量
     * @param damageType ダメージの種類
     */
    public static void damage(Entity entity, double amount, DamageType damageType) {

        if (getDEF(entity, damageType) != null)
            amount *= getDEF(entity, damageType);

        //ダメージ付与
        ((LivingEntity) entity).damage(amount);

    }

    /**
     * エンティティの近くに指定したエンティティがいるかどうかを返します。
     * @param entity エンティティ
     * @param entityType 探索対象のエンティティ
     * @param x xの範囲
     * @param y yの範囲
     * @param z zの範囲
     * @return boolean
     */
    public static boolean existEntityNear(Entity entity, EntityType entityType, double x, double y, double z) {
        for (Entity nearEntity : entity.getNearbyEntities(x, y, z)) {
            if (nearEntity.getType().equals(entityType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * エンティティの名前を体力バーに変更します。
     * @param entity 対象のエンティティ
     */
    public static void setHealthBar(Entity entity) {

        if (entity instanceof LivingEntity) {

            LivingEntity livingEntity = (LivingEntity) entity;

            double healthRate = livingEntity.getHealth() / livingEntity.getMaxHealth();
            int barAmount = (int) Math.floor(healthRate * 10);

            StringBuilder redBarBuilder = new StringBuilder();

            for (int i = 0; i < barAmount; i++) {
                redBarBuilder.append(" ");
            }

            StringBuilder spaceBarBuilder = new StringBuilder();

            for (int i = 0; i < 10 - barAmount; i++) {
                spaceBarBuilder.append(" ");
            }

            entity.setCustomName(ChatUtil.setColorCodes("&c&m" + redBarBuilder.toString() + "&8&m" + spaceBarBuilder.toString()));
            entity.setCustomNameVisible(true);
        }
    }

    /**
     * 体力バーを解除します。
     * @param entity 対象のエンティティ
     */
    public static void unsetHealthBar(Entity entity) {
        entity.setCustomNameVisible(false);

        if (MincraMagics.getMobManager().isCustomEntity(entity)) {

            String mcr_id = MobUtil.getMCRId(entity);

            if (MincraMagics.getMobManager().getEntityJSONObject(mcr_id).has("name")) {

                String name = ChatUtil.setColorCodes(MincraMagics.getMobManager().getEntityJSONObject(mcr_id).getString("name"));

                entity.setCustomName(name);

            }

        } else {
            entity.setCustomName(null);

        }
    }

    /**
     * ステータスのJSONオブジェクトを取得します。
     * @param entity 対象のエンティティ
     * @return JSONObject
     */
    public static JSONObject getStatusJSONObject(Entity entity) {
        NBTEntity nbtEntity = new NBTEntity(entity);
        NBTList<String> tagsList = nbtEntity.getStringList("Tags");

        //モブの防御力を取得
        for (String tag : tagsList) {
            if (tag.startsWith("{")) {
                return new JSONObject(tag);
            }
        }

        return null;
    }

    /**
     * エンティティのMCRIdを返します。
     * @param entity 対象のエンティティ
     * @return String
     */
    public static String getMCRId(Entity entity) {

        JSONObject jsonObject = getStatusJSONObject(entity);

        if (jsonObject != null && jsonObject.has("id")) {
            return jsonObject.getString("id");
        }
        return null;
    }

    /**
     * エンティティの耐性値を返します。
     * @param entity 対象のエンティティ
     * @return Double
     */
    public static Double getDEF(Entity entity, DamageType damageType) {

        JSONObject jsonObject = getStatusJSONObject(entity);

        if (jsonObject != null && jsonObject.has("DEF")) {

            if (jsonObject.getJSONObject("DEF").has(damageType.toString())) {

                double def = jsonObject.getJSONObject("DEF").getDouble(damageType.toString());

                //def>1だと防御力がマイナスになる
                if (def > 1)
                    return 0d;

                return 1 - def;

            }
        }
        return null;
    }
}
