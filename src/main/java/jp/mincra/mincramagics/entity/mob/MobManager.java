package jp.mincra.mincramagics.entity.mob;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.sql.SQLManager;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class MobManager extends SQLManager {

    final private List<String> FRIENDLYMOBS = new ArrayList<>(Arrays.asList("PLAYER","HORSE","OCELOT","WOLF","SHEEP","CHICKEN","COW","ITEMFRAME","VILLAGER"));

    private Map<String, JSONObject> entityJsonMap;
    private Map<EntityType, List<String>> typeMCRIDMap;
    private Map<EntityType, Integer> typeSumMap;

    public List<String> getFriendlyMobs() {
        return FRIENDLYMOBS;
    }

    public Map<String, JSONObject> getEntityJsonMap() {
        return entityJsonMap;
    }
    public List<String> getTypeMCRIDList(EntityType entityType) {
        return typeMCRIDMap.get(entityType);
    }
    public Integer getTypeSum(EntityType entityType) {
        return typeSumMap.get(entityType);
    }

    public void register(Map<String, JSONArray> jsonArrayMap) {
        entityJsonMap = new HashMap<>();
        typeMCRIDMap = new HashMap<>();
        typeSumMap = new HashMap<>();

        //読み込み
        jsonArrayMap.forEach(this::registerMob);

        ChatUtil.sendConsoleMessage(entityJsonMap.size() + "個のモブを登録しました。");
    }

    public void registerMob(String path, JSONArray jsonArray) {

        for (int i=0, len=jsonArray.length(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            //id
            if (jsonObject.has("mcr_id") && jsonObject.get("mcr_id") instanceof String) {
                String mcr_id = jsonObject.getString("mcr_id");

                entityJsonMap.put(mcr_id, jsonObject);

                if (jsonObject.has("chance")) {

                    EntityType entityType = EntityType.valueOf(jsonObject.getString("id").toUpperCase());
                    int chance = jsonObject.getInt("chance");

                    //エンティティタイプごとにchanceの合計を加算
                    typeSumMap.merge(entityType, chance, Integer::sum);

                    //mapのentityTypeのValueがnullならリスト追加
                    if (typeMCRIDMap.get(entityType) == null) {
                        List<String> list = new ArrayList<>();
                        typeMCRIDMap.put(entityType, list);
                    }
                    //エンティティタイプごとにIDをput
                    typeMCRIDMap.get(entityType).add(mcr_id);
                }

            } else {
                ChatUtil.sendConsoleMessage("エラー: " + path + "の" + i + "番目のmcr_idが不正です。");
            }
        }
    }

    public JSONObject getMobNBT(String mcr_id) {
        if (entityJsonMap.get(mcr_id).has("nbt")) {
            return entityJsonMap.get(mcr_id).getJSONObject("nbt");
        }
        return null;
    }

    /**
     * 全てのカスタムエンティティでCustomEntitySpawnを実行する
     */
    public void loadAllCustomEntity() {

        for (World world : Bukkit.getWorlds()) {

            for (Entity entity : world.getEntities()) {

                for (String tag : entity.getScoreboardTags()) {

                    if (tag != null && tag.contains("mcr_")) {
                        MincraMagics.getEventNotifier().runCustomEntitySpawn(entity, tag.substring(4));

                    }
                }
            }
        }
    }


    public boolean isExistEntity(String mcr_id) {
        if (entityJsonMap.containsKey(mcr_id)) {
            return true;
        } else {
            return false;
        }
    }

    public JSONObject getEntityJSONObject(String mcr_id) {
        return entityJsonMap.getOrDefault(mcr_id, null);
    }
}
