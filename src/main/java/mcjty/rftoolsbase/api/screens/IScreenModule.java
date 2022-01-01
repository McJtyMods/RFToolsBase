package mcjty.rftoolsbase.api.screens;

import mcjty.rftoolsbase.api.screens.data.IModuleData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * This is the server side implementation of your module. This will be called
 * sever time for your module whenever the screen needs to update information.
 * If your module doesn't need server-side information then you can just
 * return null here but you still need this module.
 *
 * @param <T>
 */
public interface IScreenModule<T extends IModuleData> {
    /**
     * Get the data that can be used client side to help render this module.
     * If you don't need data from the server side you can return null here.
     */
    T getData(IScreenDataHelper helper, Level worldObj, long millis);

    /**
     * This is called when your module is being instantiated from a saved world
     * so you can setup your data. The tags that are given to the tagCompound
     * depend on how you set up your GUI in the IClientScreenModule.
     * @param tagCompound
     * @param dim the dimension for the screen this module is in
     * @param pos the position of the screen
     */
    void setupFromNBT(CompoundTag tagCompound, ResourceKey<Level> dim, BlockPos pos);

    /**
     * How much RF/tick this module consumes
     * @return
     */
    int getRfPerTick();

    /**
     * For interactive modules you can implement this to detect if your module was clicked
     * @param world
     * @param x
     * @param y
     * @param clicked
     * @param player
     */
    void mouseClick(Level world, int x, int y, boolean clicked, Player player);

    /**
     * @return Whether this module needs a screen controller to work,
     * even in creative screens. Intended for use by things such as
     * computer screen modules, which use the controller to set their text.
     */
    default boolean needsController() {
        return false;
    }
}
