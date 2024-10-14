package mcjty.rftoolsbase.api.dimension;

import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * Get information of an RFTools dimension
 */
public interface IDimensionInformation {

    /**
     * Get the ownerUUID of this dimension.
     */
    UUID getOwner();

    /**
     * Return the current power level in the dimension
     */
    long getEnergy();

    /**
     * Return the maximum supported energy in the dimension
     */
    long getMaxEnergy(Level world);

    /**
     * Return the number of activity probes in the dimension
     */
    int getActivityProbes();
}
