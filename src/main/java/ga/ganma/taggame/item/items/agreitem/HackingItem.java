package ga.ganma.taggame.taggame.item.items.agreitem;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HackingItem implements MaterialclickTypeItemAPI {

    private boolean iscall;

    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("ハッキングツール");
            List<String> list = new ArrayList<>();
            list.add("15秒間逃げのスマホにハッキングをします。");
            list.add("15秒間はスマホに大音量で着信音が響きます。");
            im.setLore(list);
        }
        return im;
    }

    @Override
    public void run(Player useplayer) {
        Bukkit.getOnlinePlayers().forEach(
                (p) -> p.sendMessage(Taggame.prefix + useplayer.getName() +"さんがハッキングツールを使用しました！")
        );
        iscall = true;
        new BukkitRunnable(){
            @Override
            public void run() {
                if (iscall) {
                    Bukkit.getOnlinePlayers().forEach(
                            (p) -> {
                                if(Taggame.getgamemanager().getRunawayplayer().contains(p)) {
                                    p.playSound(p.getLocation().clone(), Sound.BLOCK_ANVIL_PLACE, 0.01f, 3);
                                    Bukkit.getOnlinePlayers().forEach(
                                            (pl) -> {
                                                if (p != pl) {
                                                    if (Taggame.getgamemanager().getAgrePlayer().contains(p)) {
                                                        pl.playSound(p.getLocation().clone(), Sound.BLOCK_ANVIL_PLACE, 1, 3);
                                                    }
                                                }
                                            }
                                    );
                                }
                            }
                    );
                }
            }
        }.runTaskTimer(Taggame.getPlugin(),0,2);

        new BukkitRunnable(){
            @Override
            public void run() {
                iscall = false;
            }
        }.runTaskLater(Taggame.getPlugin(),300);

        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
    }
}
