package jp.mincra.mincramagics.util;

import jp.mincra.mincramagics.MincraMagics;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {

    public static void getProperty() {
        Properties properties = new Properties();

        //プロパティファイルのパスを指定する
        String strpass = "./plugins/MincraMagics/mincra.properties";

        try {
            InputStream inputStream = new FileInputStream(strpass);
            properties.load(inputStream);
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        // Mapに格納
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            MincraMagics.getPropertyMap().put(e.getKey().toString(), e.getValue().toString());
        }
    }

    public static void setProperty() throws IOException {
        File dir = new File("./plugins/MincraMagics");
        File properties = new File("./plugins/MincraMagics/mincra.properties");

        //ディレクトリ作成
        if (dir.exists()){
            if (properties.exists() == false){
                FileWriter fileWriter = new FileWriter(properties);
                fileWriter.write("MySQL_url=");
                fileWriter.close();
                properties.createNewFile();
            }
        } else {
            dir.mkdir();

            FileWriter fileWriter = new FileWriter(properties);
            fileWriter.write("MySQL_url=");
            fileWriter.close();
            properties.createNewFile();
        }
    }
}
