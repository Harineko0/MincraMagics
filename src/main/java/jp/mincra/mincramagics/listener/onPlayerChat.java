package jp.mincra.mincramagics.listener;

import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onPlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        String translated = ChatUtil.setColorCodes(e.getMessage());
        e.setMessage(translated);

    }
}
