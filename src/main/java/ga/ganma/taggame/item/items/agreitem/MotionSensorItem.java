package ga.ganma.taggame.taggame.item.items.agreitem;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MotionSensorItem implements MaterialclickTypeItemAPI, Listener {
    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("モーションセンサー");
            List<String> list = new ArrayList<>();
            list.add("カウント後に一定時間内に動いた逃げを発光させます。");
            list.add("2回まで使用可能です。");
            list.add("クールタイムは30秒です。");
            im.setLore(list);
        }
        return im;
    }

    private static boolean issensor;
    private static boolean isOK;
    private Plugin plugin;
    private static int time;
    private HashMap<Player,Integer> counter = new HashMap<>();
    private static HashMap<Player, Location> playerlocation = new HashMap<>();

    public MotionSensorItem(Plugin plugin){
        Bukkit.getPluginManager().registerEvents(this,plugin);
        this.plugin = plugin;
        isOK = true;
    }

    @Override
    public void run(Player useplayer) {
        if (!counter.containsKey(useplayer) || counter.get(useplayer) <= 1) {
            if (isOK) {
                time = 3;
                isOK = false;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (time <= 3 && time > 0) {
                            Bukkit.getOnlinePlayers().forEach(
                                    (player) -> {
                                        player.sendTitle("", "" + time, 0, 20, 0);
                                        player.playSound(player.getLocation().clone(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 0.5f, 1);
                                    }
                            );
                            time--;
                        }
                        else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 20);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        issensor = true;
                        Bukkit.getOnlinePlayers().forEach(
                                (p) -> playerlocation.put(p,p.getLocation().clone())
                        );

                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if(issensor) {
                                    Bukkit.getOnlinePlayers().forEach(
                                            (player) -> player.playSound(player.getLocation().clone(), Sound.BLOCK_BEACON_ACTIVATE, 0.5f, 1)
                                    );
                                }
                                else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin,0,2);

                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                issensor = false;

                                new BukkitRunnable(){
                                    @Override
                                    public void run(){
                                        isOK = true;
                                    }
                                }.runTaskLater(plugin,600);

                            }
                        }.runTaskLater(plugin,100);
                    }
                }.runTaskLater(plugin, 60);
                if(counter.containsKey(useplayer)){
                    counter.put(useplayer,counter.get(useplayer) + 1);
                }
                else {
                    counter.put(useplayer,1);
                }

                if(counter.get(useplayer) >= 2){
                    useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
                }
            }
            else {
                useplayer.sendMessage(Taggame.prefix + "前回の実行から30秒経過後再度使用できます。");
            }
        }
        else {
            useplayer.sendMessage(Taggame.prefix + "すでに使用回数を超えています！");
            useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
        }
    }

    @EventHandler
    public void getplayermoveEvent(PlayerMoveEvent e){
        if(issensor){
            Player p = e.getPlayer();
            if(playerlocation.get(p).distance(p.getLocation()) > 0.3){
                if(Taggame.getgamemanager().getRunawayplayer().contains(p)){
                    PotionEffect pe = new PotionEffect(PotionEffectType.GLOWING,400,1);
                    p.addPotionEffect(pe);
                }
            }
        }
    }
}
