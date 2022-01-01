package mcjty.rftoolsbase.api.various;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Implement this interface in an item that can be used in a tablet
 */
public interface ITabletSupport {

    /// Return the item that has to be used as the tablet with this item in it. This is usually an instance of TabletItem
    Item getInstalledTablet();

    /// Open the gui for this item
    void openGui(@Nonnull Player player, @Nonnull ItemStack tabletItem, @Nonnull ItemStack containingItem);
}
