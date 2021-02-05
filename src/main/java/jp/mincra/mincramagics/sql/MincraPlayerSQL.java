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
        String query = "UPDATE player set " +
                "name = '" + mincraPlayer.getPlayerName() + "', " +
                "mp_value = " + mincraPlayer.getPlayerMP_value() + ", " +
                "cooltime_value = " + mincraPlayer.getPlayerCooltime_value() + ", " +
                "cooltime_max = " + mincraPlayer.getPlayerCooltime_max() + ", " +
                "cooltime_title = '" + mincraPlayer.getCooltimeTitle() + "'" +
                " WHERE uuid = '" + mincraPlayer.getPlayerUUID() + "'";
        executeQuery(query);
    }

    public void insertMincraPlayer(UUID uuid, MincraPlayer mincraPlayer){
        String query = "SELECT EXISTS(SELECT * FROM player WHERE uuid = '" + uuid + "')";

        //insert
        if (isExistRecord(query)){
            query = "INSERT INTO player (name, uuid, mp_value, cooltime_value, cooltime_max) VALUES ('" +
                    mincraPlayer.getPlayerName() + "', '" +
                    mincraPlayer.getPlayerUUID() + "', " +
                    mincraPlayer.getPlayerMP_value() + ", " +
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

        String sql = "SELECT name, uuid, mp_value, cooltime_value, cooltime_max, cooltime_title FROM player WHERE uuid = '"+ uuid +"'";

        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                mincraPlayer.setPlayerName(rs.getString("name"));
                mincraPlayer.setPlayerMP_value(rs.getFloat("mp_value"));
                mincraPlayer.setPlayerCooltime_value(rs.getFloat("cooltime_value"));
                mincraPlayer.setPlayerCooltime_max(rs.getFloat("cooltime_max"));
                mincraPlayer.setCooltimeTitle(rs.getString("cooltime_title"));
            }
            stmt.close();
            rs.close();
            return mincraPlayer;
        }catch (SQLException e){
            ChatUtil.sendConsoleMessage("データを取得に失敗しました。");
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

