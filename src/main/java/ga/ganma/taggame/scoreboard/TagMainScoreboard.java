package ga.ganma.taggame.taggame.scoreboard;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.mainmanager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class TagMainScoreboard {

    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private static final Scoreboard scoreboard = manager.getNewScoreboard();
    private static Objective obj;

    public static void registerScoreboard(Player p){
        if(!Taggame.gamemanagerIsempty()){
            GameManager gm = Taggame.getgamemanager();

            Team team = scoreboard.getTeam("main");
            if(team == null) {
                team = scoreboard.registerNewTeam("main");
            }
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

            team.addEntry(p.getName());
            if(obj != null) {
                obj.unregister();
            }
            obj = scoreboard.registerNewObjective(p.getName(),"dummy", ChatColor.AQUA + "情報");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score s1 = obj.getScore(ChatColor.YELLOW + "あなたの役職" + ChatColor.RESET + ":" + gm.getPlayerPosition(p));
            Score s2 = obj.getScore(ChatColor.RED + "鬼の数" + ChatColor.RESET + ":" + ChatColor.RED + gm.getAgrePlayer().size() + "人");
            Score s3 = obj.getScore(ChatColor.AQUA + "逃げの数" + ChatColor.RESET + ":" + ChatColor.AQUA + gm.getRunawayplayer().size() + "人");
            if(gm.getRunawayplayer().size() == 1){
                s3 = obj.getScore(ChatColor.AQUA + "最後の逃げ" + ChatColor.RESET + ":" + ChatColor.AQUA + gm.getRunawayplayer().iterator().next().getName());
            }
            Score s4 = obj.getScore(ChatColor.BLUE + "残り時間" + ChatColor.RESET + ":" + ChatColor.BLUE + gm.getTime() + "秒");
            Score s5 = obj.getScore("");
            Score s6 = obj.getScore(ChatColor.DARK_GREEN + "create by ganma");
            Score s7 = obj.getScore(ChatColor.GOLD + "play.freeserver.pro");

            s1.setScore(6);
            s2.setScore(5);
            s3.setScore(4);
            s4.setScore(3);
            s5.setScore(2);
            s6.setScore(1);
            s7.setScore(0);

            p.setScoreboard(scoreboard);
        }
    }
}
