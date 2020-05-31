package ga.ganma.taggame.taggame.item.items.runwayitem;

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
            im.setDisplayName("野獣の眼光（健全）");
            List<String> list = new ArrayList<>();
            list.add("鬼を10秒間光らせます。");
            im.setLore(list);
        }
        return im;
    }

    @Override
    public void run(Player useplayer) {
        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
        Bukkit.getOnlinePlayers().forEach(
                (player) ->{
                    player.sendMessage(Taggame.prefix + useplayer.getName() +"さんが野獣の眼光（健全バージョン）を使用しました！");
                    if(Taggame.getgamemanager().getAgrePlayer().contains(player)){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,1));
                    }
                }
        );
    }
}
