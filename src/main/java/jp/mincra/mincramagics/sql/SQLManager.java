package jp.mincra.mincramagics.sql;


import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.container.MincraPlayer;
import jp.mincra.mincramagics.util.ChatUtil;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class SQLManager {

    private Connection conn;
    private String url = MincraMagics.getPropertyManager().getProperty("MySQL_url")+"?user=" +
            MincraMagics.getPropertyManager().getProperty("MySQL_user")+"&password=" +
            MincraMagics.getPropertyManager().getProperty("MySQL_password");

    private MincraPlayerSQL mincraPlayerSQL;


    public Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            ChatUtil.sendConsoleMessage("MySQLの接続に失敗しました。 URL: " + url);
            ChatUtil.sendConsoleMessage("MySQLの接続に失敗しました。 URL: " + url);
            e.printStackTrace();
            return null;
        }
    }

    public boolean isExistTable(String tableName) {
        boolean tExists = false;
        try (ResultSet rs = getConnection().getMetaData().getTables(null, null, tableName, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tExists;
    }

    public boolean isExistRecord(String sql){
        //レコードの存在チェック
        int i = 0;
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                i = rs.getInt(1);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i == 0;
    }

    public void createTable(String query, String tableName) {
        try {
//            if (isExistTable(tableName)){
            Statement stmt = getConnection().createStatement();
            stmt.execute(query);
            stmt.close();
            ChatUtil.sendConsoleMessage("テーブルの作成に成功しました。 テーブル名: " + tableName);
//            } else {
//                MincraChatUtil.sendConsoleMessage("テーブルは既に存在します。 テーブル名: " + tableName);
//            }
        } catch (SQLException e) {
            ChatUtil.sendConsoleMessage("テーブルの作成に失敗しました。 \nテーブル名: " + tableName + "\nクエリ文: " + query);
            e.printStackTrace();
        }
    }

    public void createTables() {
        createTable("CREATE TABLE IF NOT EXISTS player (" +
                //AUTO_INCREMENT 値が指定されなくても自動で入力される。
                "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
                "name varchar(20), " +
                "uuid VARBINARY(36) NOT NULL UNIQUE," +
                "mp_value FLOAT, " +
                "cooltime_value FLOAT, " +
                "cooltime_max FLOAT, " +
                "cooltime_title TEXT" +
                ")", "player");
    }

    public void updateRecord(String query){
        try {
            Statement stmt = getConnection().createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRecord(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //MincraPlayer型についての操作
    public MincraPlayerSQL getMincraPlayerSQL() {
        if (mincraPlayerSQL == null) {
            mincraPlayerSQL = new MincraPlayerSQL();
        }
        return mincraPlayerSQL;
    }
}