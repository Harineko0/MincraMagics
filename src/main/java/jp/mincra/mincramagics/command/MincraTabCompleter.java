package jp.mincra.mincramagics.command;

import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MincraTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){

        if (args.length == 1) {
            final List<String> arg1List = new ArrayList<>(Arrays.asList("give","reload","cooltime","summon"));

            List<String> completions = new ArrayList<>();

            for (String arg1 : arg1List) {
                if (arg1.contains(args[0])){
                    completions.add(arg1);
                }
            }
            return completions;
        }

        if (args.length > 1) {
            switch (args[0]) {
                case "give":
                    return give(args);

                case "cooltime":
                    return cooltime(args);

                case "summon":
                    return summon(args);

            }
        }

        return null;
    }

    private List<String> give(String[] args) {
        List<String> completions = new ArrayList<>();

        //give Harineko0
        if (args.length == 3) {
            for (String key : MincraMagics.getItemManager().getItemStackMap().keySet()) {
                if (key.contains(args[2])) {
                    completions.add(key);
                }
            }
            return completions;
        }

        return null;
    }

    private List<String> cooltime(String[] args) {
        final List<String> arg2List = new ArrayList<>(Arrays.asList("set"));

        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            for (String arg : arg2List) {
                if (arg.contains(args[1])){
                    completions.add(arg);
                }
            }
            return completions;
        } else if (args.length > 3)
            switch (args[1]) {
            case "set":
                return Arrays.asList("");
        }

        return null;
    }

    private List<String> summon(String[] args) {
        List<String> completions = new ArrayList<>();

        //summon
        if (args.length == 2) {
            for (String key : MincraMagics.getMobManager().getEntityJsonMap().keySet()) {
                if (key.contains(args[1])) {
                    completions.add(key);
                }
            }
            return completions;
        }

        return null;
    }
}
