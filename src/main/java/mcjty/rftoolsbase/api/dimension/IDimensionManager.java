package mcjty.rftoolsbase.api.dimension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Get a reference to an implementation of this interface by calling:
 *         FMLInterModComms.sendFunctionMessage("rftoolsdim", "getDimensionManager", "<whatever>.YourClass$GetDimensionManager");
 */
public interface IDimensionManager {

    /**
     * Get the dimension information. If the id doesn't represent an RFTools
     * dimension this just returns null.
     * Only call this server-side
     * @param world is a reference to any world (overworld is fine)
     */
    IDimensionInformation getDimensionInformation(Level world, ResourceLocation id);
}
