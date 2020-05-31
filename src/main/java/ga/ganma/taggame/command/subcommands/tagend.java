package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tagend implements CommandAPI {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player p = (Player) sender;
        if (!Taggame.getgamemanager().getRunnable().isCancelled()) {
            Bukkit.getServer().broadcastMessage(Taggame.prefix + "ゲームが運営によって強制的に中断されました。");
            Taggame.getgamemanager().getRunnable().setWaittime(-1);
            Taggame.getgamemanager().getRunnable().settime(-1);
        }
        return false;
    }
}