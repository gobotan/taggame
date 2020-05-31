package ga.ganma.taggame.taggame.item.items.runwayitem;

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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HanakusoItem implements MaterialclickTypeItemAPI, Listener {

    private HashMap<Location,BukkitRunnable> hash = new HashMap<>();

    public HanakusoItem() {
        Bukkit.getPluginManager().registerEvents(this, Taggame.getPlugin());
    }

    @Override
    public void run(Player useplayer) {
        Location loc = useplayer.getLocation().clone();
        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
        useplayer.sendMessage(Taggame.prefix + "この座標に鼻くそを設置しました。");
        hash.put(loc,new BukkitRunnable() {
            @Override
            public void run() {
                loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_DEACTIVATE, 0.1f, 1);
                if(!Taggame.getgamemanager().isstarting()){
                    this.cancel();
                }
            }
        });
        hash.get(loc).runTaskTimer(Taggame.getPlugin(),0,15);

    }

    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            im.setDisplayName("鼻くそ");
            List<String> list = new ArrayList<>();
            list.add("右クリックした場所に鼻くそを設置します。");
            list.add("その地点に近づいた鬼はスポーン地点にぶっ飛びます。");
            im.setLore(list);
        }
        return im;
    }

    @EventHandler
    public void getPlayermoveEvent(PlayerMoveEvent e) {
        if (Taggame.getgamemanager().isstarting()) {
            Location loc = e.getTo().clone();
            if (!hash.isEmpty()) {
                hash.forEach(
                        (location,runnable) -> {
                            if (location.distance(loc) <= 4) {
                                if (Taggame.getgamemanager().getAgrePlayer().contains(e.getPlayer())) {
                                    if (!runnable.isCancelled()) {
                                        location.getWorld().createExplosion(loc, 0);
                                        e.getPlayer().teleport(Taggame.getPlugin().getConfig().getLocation("spawnlocation"));
                                        e.getPlayer().sendMessage(Taggame.prefix + "あなたは鼻くそに飛ばされました。");
                                        runnable.cancel();
                                    }
                                }
                            }
                        }
                );
            }
        }
    }
}
