package jp.mincra.mincramagics.sql;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.container.MincraEntity;
import jp.mincra.mincramagics.container.MincraPlayer;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;

public class EntitySQL extends SQLManager {

    public void insertEntity(MincraEntity mincraEntity) {
        /*
        ChatUtil.sendConsoleMessage(mincraEntity.getUuid() + mincraEntity.getMcr_id());
        String query = "SELECT EXISTS(SELECT * FROM entity WHERE uuid = '" + mincraEntity.getUuid() + "')";

        //insert
        if (isExistRecord(query)){
            query = "INSERT INTO entity (uuid, mcr_id) VALUES ('" +
                    mincraEntity.getUuid() + "', '" +
                    mincraEntity.getMcr_id() + "')";
            executeQuery(query);
        }

         */
    }

    public void deleteEntity(UUID uuid) {
        /*
        String query = "SELECT EXISTS(SELECT * FROM entity WHERE uuid = '" + uuid + "')";

        if (isExistRecord(query)) {
            query = "DELETE FROM entity WHERE uuid = '" + uuid + "'";
            executeQuery(query);
        }

         */
    }

    public void loadMincraEntity() {
        /*
        final String sql = "SELECT uuid, mcr_id FROM entity";

        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ChatUtil.sendConsoleMessage("a");
                ChatUtil.sendConsoleMessage(Bukkit.getEntity(UUID.fromString(rs.getString("uuid"))).isEmpty() + "");
                MincraMagics.getEventNotifier().runCustomEntitySpawn(Bukkit.getEntity(UUID.fromString(rs.getString("uuid"))), rs.getString("mcr_id"));

            }
            stmt.close();
            rs.close();

        }catch (SQLException e){
            ChatUtil.sendConsoleMessage("データを取得に失敗しました。");
            e.printStackTrace();

        }

         */
    }

    public void saveMincraEntity() {
        /*
        ChatUtil.sendConsoleMessage("全カスタムエンティティのデータをSQLに保存します...");

        String query = "truncate table entity";
        executeQuery(query);

        for(Map.Entry<UUID, MincraEntity> entry : MincraMagics.getMobManager().getMincraEntityMap().entrySet()) {
            insertEntity(entry.getValue());
        }

         */
    }
}
