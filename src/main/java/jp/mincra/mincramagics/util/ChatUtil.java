package jp.mincra.mincramagics.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import sun.nio.cs.UTF_8;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    private final static Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
    private final static char COLOR_CHAR = ChatColor.COLOR_CHAR;

    /**
     * カラーコードを翻訳する。&修飾子はそのまま使える。
     * @param message usage: &#FF0000&fテキスト
     */
    public static String setColorCodes(String message) {
        //Sourced from this post by imDaniX: https://github.com/SpigotMC/BungeeCord/pull/2883#issuecomment-653955600
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return ChatColor.translateAlternateColorCodes('&',matcher.appendTail(buffer).toString());
    }

    /**
     * @param msg 本文
     * @return [MincraMagics] 付きのメッセージ
     */
    public static String debug(String msg) {
        String prefix = "&#3ebef5&f[&#3de8fe&fMincraMagics&#3ebef5&f] &r";
        StringBuilder buf = new StringBuilder();
        buf.append(prefix);
        buf.append(msg);

        return setColorCodes(buf.toString());
    }

    /**
     * メッセージの対象によってコンソールに送るかプレイヤーに送るか変わる
     * @param msg 本文
     * @param listener プレイヤー
     */
    public static void sendMessage(String msg, Entity listener) {
        if (listener instanceof Player) {
            listener.sendMessage(msg);
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(msg);
        }
    }

    /**
     * コンソールにログ出力
     * @param msg 本文
     */
    public static void sendConsoleMessage(String msg) {
        String prefix = "[MincraMagics] ";
        StringBuilder buf = new StringBuilder();
        buf.append(prefix);
        buf.append(msg);

//        Bukkit.getServer().getConsoleSender().sendMessage(translateHexColorCodes(debug(msg)));
        Bukkit.getServer().getConsoleSender().sendMessage((buf.toString()));
    }

    /**
     * GoogleAppsScriptのLanguageAppを用いてメッセージを翻訳します。
     * @param message 原文
     * @param messageLanguage 原文の言語
     * @param resultLanguage 出力する言語
     * @return 翻訳された文
     * @throws UnsupportedEncodingException
     * @see <a href="https://hacknote.jp/archives/31234/">参考</a>
     */
    public static String translateMessage(String message, String messageLanguage, String resultLanguage) throws UnsupportedEncodingException {

        StringBuilder urlBuilder = new StringBuilder("https://script.google.com/macros/s/AKfycbx7tprRmlZdaOnSwhD0FNpe8u3ZtnXgogIZjTjgbf0Xz5VlOPgYLb6Dhg/exec?text=");
        urlBuilder.append(URLEncoder.encode(message, "UTF-8"));
        urlBuilder.append("&source=").append(messageLanguage).append("&target=").append(resultLanguage);
        String strURL = urlBuilder.toString();

        //StringBuilderを使って可変長の文字列を扱う
        //StringBuilderの使い方：http://www.javadrive.jp/start/stringbuilder/index1.html
        StringBuilder builder = new StringBuilder();
        //HttpClientのインスタンスを作る（HTTPリクエストを送るために必要）
        HttpClient client = new DefaultHttpClient();
        //HttpGetのインスタンスを作る（GETリクエストを送るために必要）
        System.out.println(strURL);
        HttpGet httpGet = new HttpGet(strURL);
        try {
            //リクエストしたリンクが存在するか確認するために、HTTPリクエストを送ってHTTPレスポンスを取得する
            HttpResponse response = client.execute(httpGet);
            //返却されたHTTPレスポンスの中のステータスコードを調べる
            // -> statusCodeが200だったらページが存在。404だったらNot found（ページが存在しない）。500はInternal server error。
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                //HTTPレスポンスが200よりページは存在する
                //レスポンスからHTTPエンティティ（実体）を生成
                HttpEntity entity = response.getEntity();
                //HTTPエンティティからコンテント（中身）を生成
                InputStream content = entity.getContent();
                //コンテントからInputStreamReaderを生成し、さらにBufferedReaderを作る
                //InputStreamReaderはテキストファイル（InputStream）を読み込む
                //BufferedReaderはテキストファイルを一行ずつ読み込む
                //（参考）http://www.tohoho-web.com/java/file.htm
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                //readerからreadline()で行を読んで、builder文字列(StringBuilderクラス)に格納していく。
                //※このプログラムの場合、lineは一行でなのでループは回っていない
                //※BufferedReaderを使うときは一般にこのように記述する。
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String result = builder.toString();

            if (!result.contains("Exception")) {
                JSONObject jsonObject = new JSONObject(result);

                String encode = new String(jsonObject.getString("text").getBytes("MS932"), "UTF-8");
                convertToUnicode(encode);

                return encode;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //https://qiita.com/sifue/items/039846cf8415efdc5c92
    /**
     * Unicode文字列に変換する("あ" -> "\u3042")
     * @param original
     * @return
     */
    private static String convertToUnicode(String original)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            sb.append(String.format("\\u%04X", Character.codePointAt(original, i)));
        }
        String unicode = sb.toString();
        return unicode;
    }

    /**
     * Unicode文字列から元の文字列に変換する ("\u3042" -> "あ")
     * @param unicode
     * @return
     */
    private static String convertToOiginal(String unicode)
    {
        String[] codeStrs = unicode.split("\\\\u");
        int[] codePoints = new int[codeStrs.length - 1]; // 最初が空文字なのでそれを抜かす
        for (int i = 0; i < codePoints.length; i++) {
            codePoints[i] = Integer.parseInt(codeStrs[i + 1], 16);
        }
        String encodedText = new String(codePoints, 0, codePoints.length);
        return encodedText;
    }
}