package ga.ganma.taggame.taggame.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public interface PlayerclickTypeItemAPI {
    public void run(Player fromplayer,Player toplayer);

    public ItemMeta getItemMeta();
}
