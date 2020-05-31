package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tagsetlobby implements CommandAPI {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player p = (Player) sender;
        Location loc = p.getLocation().clone();
        Taggame.getPlugin().getConfig().set("lobbylocation",loc);

        Taggame.getPlugin().saveConfig();
        Taggame.getPlugin().reloadConfig();
        p.sendMessage(Taggame.prefix + "ロビーの座標を登録しました。");
        return true;
    }
}
