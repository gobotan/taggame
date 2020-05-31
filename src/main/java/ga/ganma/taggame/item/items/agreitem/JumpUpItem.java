package ga.ganma.taggame.taggame.item.items.agreitem;

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

public class JumpUpItem implements MaterialclickTypeItemAPI {

    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName("月のうさぎの足");
            List<String> list = new ArrayList<>();
            list.add("30秒間ジャンプ力が上昇します。");
            im.setLore(list);
        }
        return im;
    }

    @Override
    public void run(Player useplayer) {
        useplayer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,600,10));
        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
        useplayer.sendMessage(Taggame.prefix + "月のうさぎの足を使用しました！");
    }
}
