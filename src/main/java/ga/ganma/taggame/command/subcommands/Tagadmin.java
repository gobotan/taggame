package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import ga.ganma.taggame.taggame.scoreboard.TagMainScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tagadmin implements CommandAPI, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player p = (Player) sender;
        if (args[1].equalsIgnoreCase("set")) {
            Player pl = Bukkit.getPlayerExact(args[2]);
            if (pl == null) {
                p.sendMessage(Taggame.prefix + "指定のプレイヤーが見つかりません！");
                return true;
            }

            Taggame.getgamemanager().setAdminPlayer(pl);
            p.sendMessage(Taggame.prefix + pl.getName() + "を運営に追加しました。");
            p.getInventory().clear();
            Bukkit.getOnlinePlayers().forEach(
                    TagMainScoreboard::registerScoreboard
            );
        } else if (args[1].equalsIgnoreCase("remove")) {
            Player pl = Bukkit.getPlayerExact(args[2]);
            if (pl == null) {
                p.sendMessage(Taggame.prefix + "指定のプレイヤーが見つかりません！");
                return true;
            }

            Taggame.getgamemanager().setRunawayplayer(pl);
            p.sendMessage(Taggame.prefix + pl.getName() + "を逃げに追加しました。");
            Bukkit.getOnlinePlayers().forEach(
                    TagMainScoreboard::registerScoreboard
            );
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (args.length == 2) {
                    if (args[1].length() == 0) {
                        return Arrays.asList("set", "remove");
                    } else {
                        if ("set".startsWith(args[1])) {
                            return Collections.singletonList("set");
                        } else if ("remove".startsWith(args[1])) {
                            return Collections.singletonList("remove");
                        }
                    }
                }
                else if(args.length == 3){
                    if(args[2].length() == 0){
                        List<String> list = new ArrayList<>();
                        Bukkit.getOnlinePlayers().forEach(
                                (player) -> list.add(player.getName())
                        );
                        return list;
                    }
                    else {
                        List<String> list = new ArrayList<>();
                        Bukkit.getOnlinePlayers().forEach(
                                (player) -> {
                                    if(player.getName().startsWith(args[2])){
                                        list.add(player.getName());
                                    }
                                }
                        );
                        return list;
                    }
                }
            }
        }
        return Taggame.getPlugin().onTabComplete(sender, command, alias, args);
    }
}
