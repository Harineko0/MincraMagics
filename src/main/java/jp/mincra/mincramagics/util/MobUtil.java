package jp.mincra.mincramagics.util;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.json.JSONObject;

public class MobUtil {

    public enum DamageType {
        Physical,
        Magic
    }

    /**
     * エンティティの防御力を計算してダメージを与えます
     * @param entity 対象のエンティティ
     * @param amount ダメージ量
     * @param damageType ダメージの種類
     */
    public static void damage(Entity entity, double amount, DamageType damageType) {

        NBTEntity nbtEntity = new NBTEntity(entity);
        NBTList<String> tagsList = nbtEntity.getStringList("Tags");
        JSONObject defObject = null;

        //モブの防御力を取得
        for (String tag : tagsList) {
            if (tag.contains("DEF")) {
                defObject = new JSONObject(tag).getJSONObject("DEF");
            }
        }

        //ダメージ計算
        if (defObject != null) {
            if (defObject.has(damageType.toString()))
                amount *= defObject.getFloat(damageType.toString());

        }

        //ダメージ付与
        ((LivingEntity) entity).damage(amount);

    }

}
