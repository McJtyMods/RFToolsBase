package mcjty.rftoolsbase.api.various;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Implement this interface in an item that can be used in a tablet
 */
public interface ITabletSupport {

    /// Return the item that has to be used as the tablet with this item in it. This is usually an instance of TabletItem
    Item getInstalledTablet();

    /// Open the gui for this item
    void openGui(@Nonnull PlayerEntity player, @Nonnull ItemStack tabletItem, @Nonnull ItemStack containingItem);
}
