package ga.ganma.taggame.taggame.item.items.runwayitem;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HideItem implements MaterialclickTypeItemAPI {
    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("社会的制裁");
            List<String> list = new ArrayList<>();
            list.add("10秒間鬼から完全に身を隠せます。");
            list.add("自分からは消えていることを確認できません。");
            im.setLore(list);
        }
        return im;
    }

    @Override
    public void run(Player useplayer) {
        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
        Bukkit.getOnlinePlayers().forEach(
                (player) ->{
                    if(Taggame.getgamemanager().getAgrePlayer().contains(player)){
                        player.hidePlayer(Taggame.getPlugin(),useplayer);
                    }
                }
        );
        useplayer.sendMessage(Taggame.prefix + "鬼から10秒間見えなくなりました！");
        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(
                        (player) ->{
                            if(Taggame.getgamemanager().getAgrePlayer().contains(player)){
                                player.showPlayer(Taggame.getPlugin(),useplayer);
                            }
                        }
                );
            }
        }.runTaskLater(Taggame.getPlugin(),200);
    }
}
