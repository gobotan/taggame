package ga.ganma.taggame.taggame.item;

import ga.ganma.taggame.taggame.Taggame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ItemHandler implements Listener {

    public ItemHandler(Plugin pl){
        Bukkit.getPluginManager().registerEvents(this,pl);
    }

    private final HashMap<Material, MaterialclickTypeItemAPI> materialclicktypeitems = new HashMap<>();
    private final HashMap<Material, PlayerclickTypeItemAPI> playerclicktypeitems = new HashMap<>();

    private static final HashMap<Player,Boolean> cooltime = new HashMap<>();

    public void register(Material material, MaterialclickTypeItemAPI materialclickTypeItemAPI){
        materialclicktypeitems.put(material, materialclickTypeItemAPI);
    }

    public void register(Material material, PlayerclickTypeItemAPI playerclickTypeItemAPI){
        playerclicktypeitems.put(material, playerclickTypeItemAPI);
    }

    public boolean itemclickexists(Material material){
        return materialclicktypeitems.containsKey(material);
    }

    public boolean playerclickexists(Material material){
        return playerclicktypeitems.containsKey(material);
    }

    public MaterialclickTypeItemAPI itemclickgetExecutor(Material material){
        if(itemclickexists(material)){
            return materialclicktypeitems.get(material);
        }
        return null;
    }

    public PlayerclickTypeItemAPI playerclickgetExecutor(Material material){
        if(itemclickexists(material)){
            return playerclicktypeitems.get(material);
        }
        return null;
    }

    @EventHandler
    public void getPlayerInteractEvent(PlayerInteractEvent e) {
        if (!cooltime.containsKey(e.getPlayer()) || cooltime.get(e.getPlayer())) {
            cooltime.put(e.getPlayer(), false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    cooltime.put(e.getPlayer(), true);
                }
            }.runTaskLater(Taggame.getPlugin(), 2);
            Material material = e.getMaterial();
            if (itemclickexists(material)) {
                if (Taggame.getgamemanager().isstarting()) {
                    itemclickgetExecutor(material).run(e.getPlayer());
                }
                else {
                    e.getPlayer().sendMessage(Taggame.prefix + "ゲーム中でのみアイテムは使用できます。");
                }
            }
        }
    }

    @EventHandler
    public void getPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        if(Taggame.getgamemanager().isstarting()){
            Player fromplayer = e.getPlayer();
            Entity toentity = e.getRightClicked();
            if(toentity instanceof Player){
                Player toplayer = (Player) toentity;
                Material material = fromplayer.getInventory().getItemInMainHand().getType();
                if(playerclickexists(material)){
                    playerclickgetExecutor(material).run(fromplayer,toplayer);
                }
            }
        }
        else {
            e.getPlayer().sendMessage(Taggame.prefix + "ゲーム中でのみアイテムは使用できます。");
        }
    }
}