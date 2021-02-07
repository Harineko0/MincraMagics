package jp.mincra.mincramagics.sql;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.container.MincraPlayer;
import jp.mincra.mincramagics.util.ChatUtil;

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
        builder.append("', ");
        //マテリアル
        StringBuilder material = new StringBuilder();
        for (int i=0; i<9; i++) {
            material.append("material0");
            material.append(i + 1);
            material.append(" = '");
            material.append(mincraPlayer.getMaterial(i));
            material.append("', ");
        }

        builder.append(material.toString());
        builder.append("materialPoint = ");
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

                for (int i = 0; i<9; i++) {
                    StringBuilder builder = new StringBuilder("material0");
                    mincraPlayer.setMaterial(i, rs.getString(builder.append(i+1).toString()));
                }
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
}

