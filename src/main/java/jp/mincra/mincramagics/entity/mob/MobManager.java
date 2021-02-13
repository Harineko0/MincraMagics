package jp.mincra.mincramagics.entity.mob;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTList;
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

    private Map<String, JSONObject> MCRIDJsonMap;
    private Map<EntityType, Integer> entityChanceSumMap;

    public List<String> getFriendlyMobs() {
        return FRIENDLYMOBS;
    }

    public Map<String, JSONObject> getMCRIDJsonMap() {
        return MCRIDJsonMap;
    }

    public void register(Map<String, JSONArray> jsonArrayMap) {
        MCRIDJsonMap = new HashMap<>();
        entityChanceSumMap = new HashMap<>();

        //読み込み
        jsonArrayMap.forEach(this::registerMob);

        ChatUtil.sendConsoleMessage(MCRIDJsonMap.size() + "個のモブを登録しました。");
    }

    /**
     * JSONファイル内のモブを登録します。
     * @param path ファイルパス
     * @param jsonArray モブデータのJSON配列
     */
    public void registerMob(String path, JSONArray jsonArray) {

        int chance = 0;

        for (int i=0, len=jsonArray.length(); i<len; i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.has("mcr_id")) {
                String mcr_id = jsonObject.getString("mcr_id");
                EntityType entityType = EntityType.valueOf(jsonObject.getString("id").toUpperCase());

                MCRIDJsonMap.put(mcr_id, jsonObject);

                if (jsonObject.has("chance")) {
                    chance += jsonObject.getInt("chance");
                }

                if (i == len - 1) {
                    //nullの場合
                    entityChanceSumMap.putIfAbsent(entityType, 0);

                    entityChanceSumMap.put(entityType, entityChanceSumMap.get(entityType)  + chance);
                }

            } else {
                ChatUtil.sendConsoleMessage("エラー: " + path + "の" + i + "番目のmcr_idが不正です。");

            }
        }
    }

    public void setEntityNBT(Entity entity, String mcr_id) {

        NBTEntity nbtEntity = new NBTEntity(entity);
        String nbtString = MCRIDJsonMap.get(mcr_id).getJSONObject("nbt").toString();

        if (nbtString != null) {
            nbtEntity.mergeCompound(new NBTContainer(nbtString));

        }
        setStatusNBT(nbtEntity, mcr_id);
    }

    /**
     * エンティティのNBTをランダムに設定します。
     * @param entity 設定するエンティティ
     */
    public void setEntityRandomNBT(Entity entity) {

        EntityType entityType = entity.getType();
        EntityType mapEntityType;
        Set<String> MCRIDSet = MCRIDJsonMap.keySet();
        int chanceSum = entityChanceSumMap.get(entityType);

        Random random = new Random();
        int chance = random.nextInt(chanceSum);

        JSONObject jsonObject;
        NBTEntity nbtEntity = new NBTEntity(entity);
        String nbtString;

        for (String mcr_id : MCRIDSet) {

            jsonObject = MCRIDJsonMap.get(mcr_id);
            mapEntityType = EntityType.valueOf(jsonObject.getString("id").toUpperCase());

            if (jsonObject.has("chance") && mapEntityType.equals(entityType)) {

                chance -= jsonObject.getInt("chance");

                if (chance < 0) {

                    //バニラのNBT
                    nbtString = MCRIDJsonMap.get(mcr_id).getJSONObject("nbt").toString();
                    if (nbtString != null) {
                        nbtEntity.mergeCompound(new NBTContainer(nbtString));
                    }
                    setStatusNBT(nbtEntity, mcr_id);

                    break;
                }
            }
        }
    }


    public boolean isCustomEntity(String mcr_id) {
        return MCRIDJsonMap.containsKey(mcr_id);
    }

    public boolean isCustomEntity(Entity entity) {

        NBTEntity nbtEntity = new NBTEntity(entity);
        NBTList<String> tagsList = nbtEntity.getStringList("Tags");

        for (String tag : tagsList) {
            if (tag.startsWith("{")) {

                JSONObject jsonObject = new JSONObject(tag);
                return MCRIDJsonMap.containsKey(jsonObject.getString("id"));

            }
        }
        return false;
    }
    /**
     * エンティティタイプがランダムスポーンリストに入っているかどうかを取得します。
     * @param entityType エンティティタイプ
     * @return boolean
     */
    public boolean isRandomSpawn(EntityType entityType) {
        return entityChanceSumMap.containsKey(entityType);
    }

    /**
     * Tagsで管理されるオリジナルNBTを設定します。
     * @param nbtEntity 対象のエンティティ
     * @param mcr_id MCRID
     */
    public void setStatusNBT(NBTEntity nbtEntity, String mcr_id) {
        //Tagsで管理するオリジナルのNBT
        NBTList<String> tagsList = nbtEntity.getStringList("Tags");

        JSONObject statusObject = new JSONObject();

        if (MCRIDJsonMap.get(mcr_id).has("status")) {
            statusObject = MCRIDJsonMap.get(mcr_id).getJSONObject("status");
        }

        String status = statusObject.toString();
        if (status != null) {
            tagsList.add(status);
        }

        statusObject.put("id", mcr_id);
    }

    public JSONObject getMobNBT(String mcr_id) {
        if (MCRIDJsonMap.get(mcr_id).has("nbt")) {
            return MCRIDJsonMap.get(mcr_id).getJSONObject("nbt");
        }
        return null;
    }


    /**
     * 全てのカスタムエンティティでCustomEntitySpawnを実行する
     */
    public void loadAllCustomEntity() {

        for (World world : Bukkit.getWorlds()) {

            NBTEntity nbtEntity;

            for (Entity entity : world.getEntities()) {

                nbtEntity = new NBTEntity(entity);
                NBTList<String> tagsList = nbtEntity.getStringList("Tags");

                for (String tag : tagsList) {

                    if (tag != null && tag.contains("id")) {
                        MincraMagics.getEventNotifier().runCustomEntitySpawn(entity, new JSONObject(tag).getString("id"));

                    }
                }
            }
        }
    }

    public JSONObject getEntityJSONObject(String mcr_id) {
        JSONObject returnObject = MCRIDJsonMap.getOrDefault(mcr_id, null);
        return returnObject;
    }

    public EntityType getEntityType(String mcr_id) {
        return EntityType.valueOf(MCRIDJsonMap.get(mcr_id).getString("id").toUpperCase());
    }
}
