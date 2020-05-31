package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import ga.ganma.taggame.taggame.scoreboard.TagMainScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Tagogre implements CommandAPI, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player p = (Player) sender;
        if(args[1].equalsIgnoreCase("set")){
            if(args.length <= 2) {
                return true;
            }
                Player pl = Bukkit.getPlayerExact(args[2]);
            if(pl == null){
                p.sendMessage(Taggame.prefix + "指定のプレイヤーが見つかりません！");
                return true;
            }

            Taggame.getgamemanager().setAgrePlayer(pl);
            p.sendMessage(Taggame.prefix + pl.getName() + "を鬼に追加しました。");
        }
        else if (args[1].equalsIgnoreCase("wp")) {
            List<Player> list = new ArrayList<>();
            for (Player pl : Bukkit.getOnlinePlayers()) {
                Location loc = pl.getLocation().clone();
                loc.setY(loc.getY() - 1);
                if (loc.getBlock().getType() == Material.EMERALD_BLOCK) {
                    list.add(pl);
                }
            }
            Random r = new Random();
            if (list.isEmpty()) {
                return true;
            }
                Player agreplayer = list.get(r.nextInt(list.size()));
                Taggame.getgamemanager().setAgrePlayer(agreplayer);
                Bukkit.getServer().broadcastMessage(Taggame.prefix + agreplayer.getName() + "さんが鬼になりました！");

        }
        else if (args[1].equalsIgnoreCase("remove")){
            Player pl = Bukkit.getPlayerExact(args[2]);
            if(pl == null){
                p.sendMessage(Taggame.prefix + "指定のプレイヤーが見つかりません！");
                return true;
            }

            Taggame.getgamemanager().setRunawayplayer(pl);
            p.sendMessage(Taggame.prefix + pl.getName() + "を逃げに追加しました。");
        }
        Bukkit.getOnlinePlayers().forEach(
                TagMainScoreboard::registerScoreboard
        );
        return false;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("ogre")) {
                if (args.length == 2) {
                    if (args[1].length() == 0) {
                        return Arrays.asList("set", "wp", "remove");
                    } else {
                        if ("set".startsWith(args[1])) {
                            return Collections.singletonList("set");
                        } else if ("remove".startsWith(args[1])) {
                            return Collections.singletonList("remove");
                        } else if("wp".startsWith(args[1])){
                            return Collections.singletonList("wp");
                        }
                    }
                }
                else if(args.length == 3 && !args[1].equalsIgnoreCase("wp")){
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
