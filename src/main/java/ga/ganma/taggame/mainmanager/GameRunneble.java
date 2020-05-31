package ga.ganma.taggame.taggame.mainmanager;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.myException.AgreNotFoundException;
import ga.ganma.taggame.taggame.scoreboard.TagMainScoreboard;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunneble extends BukkitRunnable {

    private static BossBar bossBar = Bukkit.getServer().createBossBar("残り時間:" + Taggame.getgamemanager().getTime() + "秒", BarColor.BLUE, BarStyle.SOLID);
    private static int beginTime;
    private int waitsecond = 60;
    private GameManager gm = Taggame.getgamemanager();

    public GameRunneble(int begintime){
        this.beginTime = begintime;
    }

    @Override
    public void run() {

        if(waitsecond >= 0){
            if(waitsecond != 0 && waitsecond <= 10){
                Bukkit.getOnlinePlayers().forEach(
                        (p) -> {
                            p.sendTitle("", waitsecond + "秒前", 0, 20, 0);
                                    p.playSound(p.getLocation(),Sound.UI_BUTTON_CLICK,1,2);
                        }
                );
            }
            else if(waitsecond == 0){
                Bukkit.getOnlinePlayers().forEach(
                        (p) -> {
                            p.sendTitle("スタート！","",20,60,40);
                            p.playSound(p.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,0.6f,1);
                        }
                );
            }
            TextComponent tc = new TextComponent(waitsecond + "秒後に鬼が動き出します！");
            Bukkit.getOnlinePlayers().forEach(
                    (player) -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR,tc)
            );
            waitsecond--;
            return;
        }

        if (gm.getTime() > 0) {
            if (gm.getRunawayplayer().size() == 0) {
                //鬼の勝利
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.BLUE  + "ゲームが終了しました！",
                            ChatColor.RED + "鬼の勝利です！",10,60,10);
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,0.5f,1);
                }
                gm.setstarted(false);
                this.cancel();
                bossBar.removeAll();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        gm.end();
                    }
                }.runTaskLater(Taggame.getPlugin(),200);
                return;
            }

            //例外を送出することでなぜゲームが中断したか分かりやすくする
            try {
                isgameproceed();
            } catch (AgreNotFoundException e) {
                Taggame.getPlugin().getLogger().warning(Taggame.prefix + "ゲーム中に鬼がいなくなりました。ゲームを強制的に中断します。");
                Bukkit.getServer().broadcastMessage(Taggame.prefix + "ゲーム中に鬼がいなくなりました。ゲームを強制的に中断します。");
                bossBar.removeAll();
                this.cancel();

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        gm.end();
                    }
                }.runTaskLater(Taggame.getPlugin(),100);
                return;
            }
        }
        else if(gm.getTime() == 0){
            //逃げの勝利
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendTitle(ChatColor.BLUE  + "ゲームが終了しました！",
                        ChatColor.AQUA + "逃げの勝利です！",10,60,10);
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,0.5f,1);
            }
            this.cancel();
            Bukkit.getOnlinePlayers().forEach(
                    TagMainScoreboard::registerScoreboard
            );
            gm.setstarted(false);
            bossBar.removeAll();
            new BukkitRunnable(){
                @Override
                public void run() {
                    gm.end();
                }
            }.runTaskLater(Taggame.getPlugin(),200);
            return;
        }
        else {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendTitle(ChatColor.BLUE  + "ゲームが終了しました！",
                        ChatColor.RED + "（運営のせいで）",10,60,10);
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,0.5f,1);
            }
            bossBar.removeAll();
            this.cancel();
            Bukkit.getOnlinePlayers().forEach(
                    TagMainScoreboard::registerScoreboard
            );
            gm.setstarted(false);
            gm.end();
            return;
        }

        gm.Reducetime();
        gm.setstarted(true);
        gm.setmove(true);
        bossBar.setTitle("残り時間:" + gm.getTime() / 60 + "分" + (gm.getTime() % 60 < 10? "0" + gm.getTime() % 60 : gm.getTime() % 60) + "秒");
        bossBar.setProgress(gm.getTime()/(double)beginTime);

        //プレイヤーごとにスコアボードの表示・更新
        Bukkit.getOnlinePlayers().forEach(
                (player) -> {
                    TagMainScoreboard.registerScoreboard(player);
                    bossBar.addPlayer(player);
                }

        );

    }

    //ゲームが続行可能かどうか
    private void isgameproceed() throws AgreNotFoundException{
        GameManager manager = Taggame.getgamemanager();
        if(!manager.isAgredecided()){
            throw new AgreNotFoundException();
        }
    }

    public void settime(int time){
        gm.setTime(time);
        if(beginTime < time){
            beginTime = time;
        }
    }

    public void setWaittime(int time){
        this.waitsecond = time;
    }
}
