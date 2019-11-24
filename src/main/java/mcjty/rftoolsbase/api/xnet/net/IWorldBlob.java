package mcjty.rftoolsbase.api.xnet.net;

import mcjty.rftoolsbase.api.xnet.keys.ConsumerId;
import mcjty.rftoolsbase.api.xnet.keys.NetworkId;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * This represents the network connectivity for a given dimension. There is one IWorldBlob for
 * every dimension (that holds XNet devices). You can get a reference to the world blob
 * by calling IXnet.getWorldBlob()
 */
public interface IWorldBlob {

    /**
     * Call this to let the controller/router recalculate its internal caches in case
     * an update to a network was done. Normally XNet does this automatically but you may
     * want to call this if you have your own IConsumerProvider
     */
    void markNetworkDirty(NetworkId id);

    /**
     * Get the network ids that are valid for a given location in the world. Returns
     * an empty set if there is no network there. Note that a router can have multiple
     * networks but controllers, cables and connectors only have one
     */
    @Nonnull
    Set<NetworkId> getNetworksAt(@Nonnull BlockPos pos);

    /**
     * Find the position of the network provider (the controller or router).
     * If it is a controller you can access it by casting the tile entity
     * to IControllerContext.
     */
    @Nullable
    BlockPos getProviderPosition(@Nonnull NetworkId networkId);

    /**
     * Get all consumers (connectors) on a given network
     */
    @Nonnull
    Set<BlockPos> getConsumers(NetworkId network);

    /**
     * Get the position of a consumer
     */
    @Nullable
    BlockPos getConsumerPosition(@Nonnull ConsumerId consumer);

    /**
     * Get the consumer ID for a given position. A consumer basically corresponds
     * with a connector
     */
    @Nullable
    ConsumerId getConsumerAt(@Nonnull BlockPos pos);

}
