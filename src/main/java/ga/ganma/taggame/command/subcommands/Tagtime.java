package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import ga.ganma.taggame.taggame.scoreboard.TagMainScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Tagtime implements CommandAPI, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player p = (Player) sender;
        int a;
        if (args[1].equalsIgnoreCase("set") && args.length > 2) {
            try {
                a = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                p.sendMessage(Taggame.prefix + "数字を入力してください。");
                return false;
            }

            Taggame.getgamemanager().setTime(a);
            p.sendMessage(Taggame.prefix + a + "秒に設定しました。");

        }
        Bukkit.getOnlinePlayers().forEach(
                TagMainScoreboard::registerScoreboard
        );
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("time")) {
                if (args.length == 2) {
                    if (args[1].length() == 0) {
                        return Collections.singletonList("set");
                    } else {
                        if ("set".startsWith(args[1])) {
                            return Collections.singletonList("set");
                        }
                    }
                }
            }
        }
        Bukkit.getOnlinePlayers().forEach(
                TagMainScoreboard::registerScoreboard
        );
        return Taggame.getPlugin().onTabComplete(sender, command, alias, args);
    }
}
