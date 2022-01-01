package mcjty.rftoolsbase.api.xnet.channels;

import mcjty.rftoolsbase.api.xnet.keys.NetworkId;
import mcjty.rftoolsbase.api.xnet.net.IWorldBlob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Implement this interface if you want to implement your own consumer provider.
 * The controller will use this to allow you to add additional consumers (connectors)
 * Register this with IXNet.
 */
public interface IConsumerProvider {

    /**
     * The controller will call this whenever it needs to update its list of consumers. Note that
     * the controller will cache this so make sure to call IWorldBlob.markNetworkDirty() on the
     * given network
     */
    @Nonnull
    Set<BlockPos> getConsumers(Level world, IWorldBlob worldBlob, NetworkId networkId);
}
