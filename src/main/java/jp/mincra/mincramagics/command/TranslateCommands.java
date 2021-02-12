package jp.mincra.mincramagics.command;

import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TranslateCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Entity) {

            if (args.length > 2) {

                Player player = (Player) sender;
                List<String> langList = Arrays.asList(Locale.getISOLanguages());

                if (langList.contains(args[0]) && langList.contains(args[1])) {

                    StringBuilder message = new StringBuilder(args[2]);

                    for (int i=3, len=args.length; i<len; i++) {
                        message.append(" ").append(args[i]);

                    }

                    try {
                        String translated = ChatUtil.translateMessage(message.toString(), args[0], args[1]);

                        if (translated != null) {
                            StringBuilder builder = new StringBuilder(translated);
                            builder.append(" &7(");
                            builder.append(message.toString());
                            builder.append(")");

                            player.chat(ChatUtil.setColorCodes(builder.toString()));
                        }

                        return true;


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
