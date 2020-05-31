package ga.ganma.taggame.taggame.item.items.agreitem;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class GlowItem implements MaterialclickTypeItemAPI {

    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("野獣の眼光");
            List<String> list = new ArrayList<>();
            list.add("5秒間逃げを発光させます。");
            im.setLore(list);
        }
        return im;
    }

    @Override
    public void run(Player useplayer) {
        Bukkit.getOnlinePlayers().forEach(
                (p) -> {
                    p.sendMessage(Taggame.prefix + useplayer.getName() +"さんが野獣の眼光を使用しました！");
                    if(Taggame.getgamemanager().getRunawayplayer().contains(p)){
                        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,100,1));
                    }
                }
        );
        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
    }
}
