package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import ga.ganma.taggame.taggame.mainmanager.GameManager;
import ga.ganma.taggame.taggame.myException.AgreNotFoundException;
import ga.ganma.taggame.taggame.myException.NoTimeSetException;
import ga.ganma.taggame.taggame.myException.RunawayNotFoundException;
import ga.ganma.taggame.taggame.scoreboard.TagMainScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tagstart implements CommandAPI {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (!Taggame.getgamemanager().isstarting()) {
            Player p = (Player) sender;
            GameManager gm = Taggame.getgamemanager();
            try {
                gm.start(Taggame.getPlugin().getConfig().getLocation("spawnlocation"));
            } catch (NoTimeSetException e) {
                p.sendMessage(Taggame.prefix + "時間が設定されていません！");
            } catch (AgreNotFoundException e) {
                p.sendMessage(Taggame.prefix + "鬼が決められていません！");
            } catch (RunawayNotFoundException e) {
                p.sendMessage(Taggame.prefix + "逃げるプレイヤーが存在しません！");
            }
        }
        Bukkit.getOnlinePlayers().forEach(
                TagMainScoreboard::registerScoreboard
        );
        return false;
    }
}
