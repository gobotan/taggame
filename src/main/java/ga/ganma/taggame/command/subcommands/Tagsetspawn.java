package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Tagsetspawn implements CommandAPI {

    private Plugin plugin;

    public Tagsetspawn(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player p = (Player) sender;
        Location loc = p.getLocation().clone();
        Taggame.getPlugin().getConfig().set("spawnlocation",loc);

        Taggame.getPlugin().saveConfig();
        Taggame.getPlugin().reloadConfig();
        p.sendMessage(Taggame.prefix + "ゲーム中にスポーンする座標を登録しました。");
        return true;
    }
}
