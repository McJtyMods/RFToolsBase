package mcjty.rftoolsbase.api.screens;

import mcjty.rftoolsbase.api.screens.data.IModuleData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

/**
 * This is the server side implementation of your module. This will be called
 * sever time for your module whenever the screen needs to update information.
 * If your module doesn't need server-side information then you can just
 * return null here but you still need this module.
 *
 * @param <T>
 */
public interface IScreenModule<M extends IScreenModule<?, T>, T extends IModuleData> {
    /**
     * Get the data that can be used client side to help render this module.
     * If you don't need data from the server side you can return null here.
     */
    T getData(IScreenDataHelper helper, Level worldObj, long millis);

    /**
     * This is called after loading the module. It will validate the data
     * and enable/disable the module depending on the data.
     */
    M validate(Level world, BlockPos pos, boolean isPlus);

    /**
     * How much RF/tick this module consumes
     * @return
     */
    int getRfPerTick();

    /**
     * For interactive modules you can implement this to detect if your module was clicked
     * @param moduleStack is the module itemstack
     * @param world
     * @param x
     * @param y
     * @param clicked
     * @param player
     * @return a new ItemStack if you want to change the module itemstack, otherwise ItemStack.EMPTY
     */
    @Nonnull
    ItemStack mouseClick(ItemStack moduleStack, Level world, int x, int y, boolean clicked, Player player);

    /**
     * @return Whether this module needs a screen controller to work,
     * even in creative screens. Intended for use by things such as
     * computer screen modules, which use the controller to set their text.
     */
    default boolean needsController() {
        return false;
    }
}
