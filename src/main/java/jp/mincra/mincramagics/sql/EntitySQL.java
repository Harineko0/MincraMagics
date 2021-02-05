package jp.mincra.mincramagics.sql;

import jp.mincra.mincramagics.container.MincraEntity;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class EntitySQL extends SQLManager {

    /**
     * 全てのカスアムエンティティのUUIDとmcr_idをSQLに保存する
     */
    public void saveAllCustomEntity() {
        ChatUtil.sendConsoleMessage("エンティティの保存を開始します...");

        for (World world : Bukkit.getWorlds()) {

            for (Entity entity : world.getEntities()) {

                UUID uuid = entity.getUniqueId();
                String mcr_id = null;
                for (String tag : entity.getScoreboardTags()) {
                    if (tag.contains("mcr_")) {
                        mcr_id = tag;
                    }
                }

                //mcr_idがあるとき=カスタムエンティティのとき
                if (mcr_id != null) {

                    StringBuffer buffer = new StringBuffer("INSERT INTO entity (mcr_id, uuid) VALUES ('");
                    buffer.append(mcr_id);
                    buffer.append("', '");
                    buffer.append(uuid);
                    buffer.append("')");

                    executeQuery(buffer.toString());
                }

            }
        }
    }


    /**
     * カスアムエンティティのデータを取得する。
     */
    private MincraEntity getMincraEntity(UUID uuid) {

        StringBuilder exist = new StringBuilder("SELECT EXISTS(SELECT * FROM entity WHERE uuid = '");
        exist.append(uuid.toString());
        exist.append("')");

        if (isExistRecord(exist.toString())) {

            StringBuilder query = new StringBuilder("SELECT mcr_id, uuid FROM entity WHERE uuid ='");
            query.append(uuid);
            query.append("'");

            try {
                MincraEntity mincraEntity = new MincraEntity();

                Statement stmt = getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(query.toString());
                while (rs.next()) {
                    mincraEntity.setUuid(uuid);
                    mincraEntity.setMcr_id(rs.getString("mcr_id"));

                    ChatUtil.sendConsoleMessage(rs.getString("mcr_id"));

                }
                stmt.close();
                rs.close();
                return mincraEntity;
            } catch (SQLException e) {
                ChatUtil.sendConsoleMessage("データの取得に失敗しました。");
                e.printStackTrace();

                return null;
            }
        }

        return null;
    }
}
