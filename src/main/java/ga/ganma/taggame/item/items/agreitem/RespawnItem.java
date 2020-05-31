package ga.ganma.taggame.taggame.item.items.agreitem;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RespawnItem implements MaterialclickTypeItemAPI {
    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("リスポ玉");
            List<String> list = new ArrayList<>();
            list.add("5秒後にリスポーン地点にテレポートします。");
            im.setLore(list);
        }
        return im;
    }

    private static HashMap<Player, Boolean> cooltime = new HashMap<>();

    @Override
    public void run(Player useplayer) {
        if (!cooltime.containsKey(useplayer) || cooltime.get(useplayer)) {
            useplayer.sendMessage(Taggame.prefix + "5秒後にリスポーンします。");
            cooltime.put(useplayer,false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    useplayer.teleport(Taggame.getPlugin().getConfig().getLocation("spawnlocation"));
                    cooltime.put(useplayer,true);
                }
            }.runTaskLater(Taggame.getPlugin(), 100);
        }
    }
}
