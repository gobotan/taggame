package ga.ganma.taggame.taggame;

import ga.ganma.taggame.taggame.command.CommandHandler;
import ga.ganma.taggame.taggame.command.MainCommand;
import ga.ganma.taggame.taggame.command.subcommands.*;
import ga.ganma.taggame.taggame.listener.GameMainlistener;
import ga.ganma.taggame.taggame.mainmanager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Taggame extends JavaPlugin {

    private static Plugin plugin;
    public static final String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "taggame" + ChatColor.GRAY + "]" + ChatColor.RESET;
    private static GameManager manager;

    @Override
    public void onEnable() {
        plugin = this;
        registercommands();
        registerEvents();
        saveDefaultConfig();

        registerGamemanager();

        Bukkit.getOnlinePlayers().forEach(
                (player) -> Taggame.getgamemanager().setRunawayplayer(player)
        );
        getLogger().info(prefix + "鬼ごっこプラグインが起動しました。");
    }

    //わかりやすくするため
    public static Plugin getPlugin(){
        return plugin;
    }

    //onEnableにこれ書くのはきしょいので別メソッドに
    private void registercommands(){
        CommandHandler handler = new CommandHandler(this);

        handler.regiseter("tag",new MainCommand());

        handler.regiseter("admin", new Tagadmin(), new Tagadmin());
        handler.regiseter("end",new tagend());
        handler.regiseter("help", new Taghelp());
        handler.regiseter("ogre", new Tagogre(), new Tagogre());
        handler.regiseter("runaway", new Tagrunaway(), new Tagrunaway());
        handler.regiseter("start", new Tagstart());
        handler.regiseter("time", new Tagtime(), new Tagtime());
        handler.regiseter("setspawn", new Tagsetspawn(this));
        handler.regiseter("setlobby",new Tagsetlobby());

        getCommand("tag").setExecutor(handler);
    }

    private void registerEvents(){
        new GameMainlistener(this);
    }

    public static GameManager registerGamemanager(){
        if(manager != null) {
            manager = null;
        }
        manager = new GameManager();
        return manager;
    }

    public static GameManager getgamemanager(){
        return manager;
    }

    public static boolean gamemanagerIsempty(){
        return manager == null;
    }

}
