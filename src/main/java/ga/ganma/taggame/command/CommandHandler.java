package ga.ganma.taggame.taggame.command;

import ga.ganma.taggame.taggame.Taggame;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private static HashMap<String, CommandAPI> commands = new HashMap<>();
    private static HashMap<String, TabCompleter> tabcommands = new HashMap<>();
    private Plugin plugin;

    public CommandHandler(Plugin pl) {
        this.plugin = pl;
    }

    public void regiseter(String name, CommandAPI cmd, TabCompleter tab) {
        commands.put(name, cmd);
        if (tab != null) {
            tabcommands.put(name, tab);
        }
    }

    public void regiseter(String name, CommandAPI cmd) {
        commands.put(name, cmd);
    }

    public boolean exists(String name) {
        return commands.containsKey(name);
    }

    public boolean tabexists(String name) {
        return tabcommands.containsKey(name);
    }

    public CommandAPI getExecutor(String name) {
        return commands.get(name);
    }

    public TabCompleter gettabExecuter(String name) {
        return tabcommands.get(name);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                if (args.length == 0) {
                    getExecutor("tag").onCommand(sender, command, label, args);
                    return true;
                }
                if (args.length > 0) {
                    if (exists(args[0])) {
                        getExecutor(args[0]).onCommand(sender, command, label, args);
                        return true;
                    } else {
                        sender.sendMessage(Taggame.prefix + "そのコマンドは存在しません！");
                    }
                }
            }
            else {
                getExecutor("tag").onCommand(sender, command, label, args);
                return true;
            }
            if (exists(args[0])) {
                if (args[0].equalsIgnoreCase("help")) {
                        getExecutor(args[0]).onCommand(sender, command, label, args);
                }
            }
        } else {
            Bukkit.getLogger().info(Taggame.prefix + "鬼ごっこプラグイン関連のコマンドは必ずゲーム内から実行してください。");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("tag")) {
            return plugin.onTabComplete(sender, command, alias, args);
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length == 1) {
                    if (args[0].length() == 0) {
                        List<String> list = new ArrayList<>();
                        commands.forEach(
                                (key, value) -> list.add(key)
                        );
                        return list;
                    }
                    List<String> list = new ArrayList<>();
                    for (String s : commands.keySet()) {
                        if (s.startsWith(args[0])) {
                            list.add(s);
                        }
                    }
                    return list;
                } else if (args.length >= 2) {
                    if (tabexists(args[0])) {
                        return gettabExecuter(args[0]).onTabComplete(sender, command, alias, args);
                    }
                }
            }
        }
        return plugin.onTabComplete(sender, command, alias, args);
    }
}
