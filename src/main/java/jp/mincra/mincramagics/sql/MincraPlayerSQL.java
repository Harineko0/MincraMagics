package jp.mincra.mincramagics.sql;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.container.MincraPlayer;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;

public class MincraPlayerSQL extends SQLManager {

    //MincraPlayer型についての操作
    public void updateMincraPlayer(MincraPlayer mincraPlayer){

        StringBuilder builder = new StringBuilder("UPDATE player set name = '");
        builder.append(mincraPlayer.getPlayerName());
        builder.append("', cooltime_value = ");
        builder.append(mincraPlayer.getPlayerCooltime_value());
        builder.append(", cooltime_max = ");
        builder.append(mincraPlayer.getPlayerCooltime_max());
        builder.append(", cooltime_title = '");
        builder.append(mincraPlayer.getCooltimeTitle());
        builder.append("', materialInventory = '");

        StringBuilder titleBuilder = new StringBuilder("&#2d9ebd&f&lマテリアル &8残りスキルポイント: ");
        titleBuilder.append(mincraPlayer.getMaterialPoint());

        builder.append(inventoryToString(mincraPlayer.getMaterialInventory(), titleBuilder.toString()));
        builder.append("', materialPoint = ");
        builder.append(mincraPlayer.getMaterialPoint());
        builder.append(" WHERE uuid = '");
        builder.append(mincraPlayer.getPlayerUUID());
        builder.append("'");

        String query = builder.toString();

        executeQuery(query);
    }

    public void insertMincraPlayer(UUID uuid, MincraPlayer mincraPlayer){
        String query = "SELECT EXISTS(SELECT * FROM player WHERE uuid = '" + uuid + "')";

        //insert
        if (isExistRecord(query)){
            query = "INSERT INTO player (name, uuid, cooltime_value, cooltime_max) VALUES ('" +
                    mincraPlayer.getPlayerName() + "', '" +
                    mincraPlayer.getPlayerUUID() + "', " +
                    mincraPlayer.getPlayerCooltime_value() + ", " +
                    mincraPlayer.getPlayerCooltime_max() + ")";
            executeQuery(query);
        } else {
            ChatUtil.sendConsoleMessage("レコードが既にplayerテーブルに存在しています。 name=" +
                    mincraPlayer.getPlayerName() + " UUID: " + mincraPlayer.getPlayerUUID());
        }
    }

    public MincraPlayer getMincraPlayer(UUID uuid) {
        MincraPlayer mincraPlayer = new MincraPlayer();
        mincraPlayer.setPlayerUUID(uuid);

        String sql = "SELECT * FROM player WHERE uuid = '"+ uuid +"'";

        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                mincraPlayer.setPlayerName(rs.getString("name"));
                mincraPlayer.setPlayerCooltime_value(rs.getFloat("cooltime_value"));
                mincraPlayer.setPlayerCooltime_max(rs.getFloat("cooltime_max"));
                mincraPlayer.setCooltimeTitle(rs.getString("cooltime_title"));
                mincraPlayer.setMaterialInventory(stringToInventory(ChatUtil.setColorCodes(rs.getString("materialInventory"))));
                mincraPlayer.setMaterialPoint(rs.getInt("materialPoint"));
            }
            stmt.close();
            rs.close();
            return mincraPlayer;
        }catch (SQLException e){
            ChatUtil.sendConsoleMessage("データの取得に失敗しました。");
            e.printStackTrace();

            return null;
        }
    }

    public void saveMincraPlayer(){
        ChatUtil.sendConsoleMessage("全プレイヤーのデータをSQLに保存します...");

        for(Map.Entry<UUID, MincraPlayer> entry : MincraMagics.getPlayerManager().getMincraPlayerMap().entrySet()) {
            updateMincraPlayer(entry.getValue());
        }
    }

    //inventory
    private String inventoryToString(Inventory inventory, String title) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        ItemStack itemStack;
        NBTItem nbtItem;

        for (int i=0, len=inventory.getSize(); i<len; i++) {
            JSONObject inventoryObject = new JSONObject();
            itemStack = inventory.getItem(i);

            if (itemStack != null) {
                nbtItem = new NBTItem(itemStack);

                inventoryObject.put("count", itemStack.getAmount());
                inventoryObject.put("slot", i);
                inventoryObject.put("id", itemStack.getType());

                if (nbtItem.hasNBTData()) {
                    inventoryObject.put("tag", nbtItem.toString());
                }

                jsonArray.put(inventoryObject);

            }
        }

        jsonObject.put("size", inventory.getSize());
        jsonObject.put("title", title);
        jsonObject.put("inventory", jsonArray);

        return jsonObject.toString();
    }

    private Inventory stringToInventory(String string) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(string);
        }catch (JSONException e){
            ChatUtil.sendConsoleMessage("エラー: "+e.toString());
        }

        Inventory inventory = Bukkit.createInventory(null, jsonObject.getInt("size"), jsonObject.getString("title"));

        JSONArray jsonArray = jsonObject.getJSONArray("inventory");

        for (int i=0, len=jsonArray.length(); i<len; i++) {
            JSONObject inventoryObject = jsonArray.getJSONObject(i);

            ItemStack item = null;
            item.setType(Material.valueOf(inventoryObject.getString("id")));
            item.setAmount(inventoryObject.getInt("count"));

            NBTItem nbt = new NBTItem(item);
            nbt.mergeCompound(new NBTContainer(inventoryObject.getJSONObject("tag").toString()));

            item = nbt.getItem();

            inventory.setItem(i, item);

        }

        return inventory;
    }
}

