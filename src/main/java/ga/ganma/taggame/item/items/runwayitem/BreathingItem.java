package ga.ganma.taggame.taggame.item.items.runwayitem;

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

public class BreathingItem implements MaterialclickTypeItemAPI {
    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("イルカの骨粉");
            List<String> list = new ArrayList<>();
            list.add("1分間水中での泳ぎが早くなります。");
            im.setLore(list);
        }
        return im;
    }

    @Override
    public void run(Player useplayer) {
        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
        useplayer.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,1200,1));
        useplayer.sendMessage(Taggame.prefix + "水の中での泳ぎが1分間早くなりました。");
    }
}
