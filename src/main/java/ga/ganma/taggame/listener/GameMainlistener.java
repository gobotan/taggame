package ga.ganma.taggame.taggame.listener;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.scoreboard.TagMainScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class GameMainlistener implements Listener {

    private Plugin plugin;
    private HashMap<Player, Boolean> isRunneble = new HashMap<>();

    public GameMainlistener(Plugin pl) {
        Bukkit.getPluginManager().registerEvents(this, pl);
        this.plugin = pl;
    }

    @EventHandler
    public void getJoinPlayer(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!Taggame.getgamemanager().isstarting()) {
            Taggame.getgamemanager().setRunawayplayer(p);
            p.sendMessage(Taggame.prefix + "あなたを逃げに追加しました。");
            Bukkit.getOnlinePlayers().forEach(
                    TagMainScoreboard::registerScoreboard
            );
            p.getInventory().clear();
            Location loc = Taggame.getPlugin().getConfig().getLocation("lobbylocation");
            p.setGameMode(GameMode.ADVENTURE);
            if(loc != null){
                p.teleport(loc);
            }
        } else {
            Taggame.getgamemanager().setAgrePlayer(p);
            p.teleport(Taggame.getPlugin().getConfig().getLocation("spawnlocation"));
            p.sendMessage(Taggame.prefix + "すでにゲームが始まっているためあなたを鬼に追加しました。");
            p.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void getPlayerDamageEvent(EntityDamageByEntityEvent e) {
        if (Taggame.getgamemanager().isstarting()) {
            Entity toentity = e.getEntity();
            Entity fromentity = e.getDamager();

            if (toentity instanceof Player && fromentity instanceof Player) {
                Player toplayer = (Player) toentity;
                Player fromplayer = (Player) fromentity;

                if (Taggame.getgamemanager().getRunawayplayer().contains(toplayer)
                        && Taggame.getgamemanager().getAgrePlayer().contains(fromplayer)) {
                    toplayer.sendMessage(Taggame.prefix + "あなたは鬼に捕まりました！");
                    toplayer.sendMessage(Taggame.prefix + "3秒後に鬼としてリスポーンします！");
                    Bukkit.getServer().broadcastMessage(Taggame.prefix + toplayer.getName() + "さんが鬼に捕まりました！");
                    Taggame.getgamemanager().setAgrePlayer(toplayer);
                    PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS, 20*4, 1);
                    toplayer.addPotionEffect(pe);
                    e.setCancelled(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            toplayer.teleport(Taggame.getPlugin().getConfig().getLocation("spawnlocation"));
                        }
                    }.runTaskLater(plugin, 60);
                }
            }
            e.setCancelled(true);
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void getPlayermoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Taggame.getgamemanager().getAgrePlayer().contains(p) && !Taggame.getgamemanager().ismoveing()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void getPlayerleaveEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Taggame.getgamemanager().playerquit(p);
    }

    @EventHandler
    public void getPlayerInventoryclickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (Taggame.getgamemanager().isstarting()) {
            if (Taggame.getgamemanager().getAgrePlayer().contains(p)) {
                if (e.getCurrentItem() != null) {
                    ItemStack is = e.getCurrentItem();
                    if (is.getType() == Material.DIAMOND_CHESTPLATE || is.getType() == Material.DIAMOND_HELMET) {
                        e.setCancelled(true);
                    }
                }
            }
        }
        else if (Taggame.getgamemanager().getAgrePlayer().contains(p)) {
            if (e.getCurrentItem() != null) {
                ItemStack is = e.getCurrentItem();
                if (is.getType() == Material.DIAMOND_CHESTPLATE || is.getType() == Material.DIAMOND_HELMET) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void getPlayerstaminaEvent(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void getDamageevent(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        } else {
            e.setDamage(0);
        }
    }

   // @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (!e.isSneaking() && Taggame.getgamemanager().isstarting()) {
            e.setCancelled(true);
            p.setSneaking(true);
        }
    }

    @EventHandler
    public void getdustItem(PlayerDropItemEvent e){
            e.setCancelled(true);
    }

    @EventHandler
    public void getentityinteractevent(EntityInteractEvent e){
        e.setCancelled(true);
    }
}
