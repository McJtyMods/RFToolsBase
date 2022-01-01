package mcjty.rftoolsbase.api.various;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Implement this on an item for which you want the item cycle keys to work ([ and ] by default)
 */
public interface IItemCycler {

    void cycle(Player player, ItemStack stack, boolean next);
}
