package mcjty.rftoolsbase.api.various;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Implement this on an item for which you want the item cycle keys to work ([ and ] by default)
 */
public interface IItemCycler {

    void cycle(PlayerEntity player, ItemStack stack, boolean next);
}
