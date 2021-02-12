package jp.mincra.mincramagics.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TranslateTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 3) {

            List<String> langList = Arrays.asList(Locale.getISOLanguages());

            List<String> completions = new ArrayList<>();

            for (String arg : langList) {
                if (arg.startsWith(args[args.length-1])){
                    completions.add(arg);
                }
            }
            return completions;
        }

        return null;

    }
}
