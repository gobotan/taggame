package ga.ganma.taggame.taggame.item.items.commonItem;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpeedItem implements MaterialclickTypeItemAPI {

    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("レッドブル");
            List<String> list = new ArrayList<>();
            list.add("翼を授かり20秒間スピードがあがります。");
            im.setLore(list);
        }
        return im;
    }

    @Override
    public void run(Player useplayer) {
        useplayer.getInventory().setItemInMainHand(null);
        useplayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,3));
        useplayer.sendMessage(Taggame.prefix + "20秒間足が早くなりました！");
    }
}
