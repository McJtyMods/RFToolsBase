package mcjty.rftoolsbase.api.xnet;

import mcjty.rftoolsbase.api.xnet.channels.IChannelType;
import mcjty.rftoolsbase.api.xnet.channels.IConnectable;
import mcjty.rftoolsbase.api.xnet.channels.IConsumerProvider;
import mcjty.rftoolsbase.api.xnet.net.IWorldBlob;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Main interface for XNet.
 * Get a reference to an implementation of this interface by calling:
 *         FMLInterModComms.sendFunctionMessage("xnet", "getXNet", "<whatever>.YourClass$GetXNet");
 */
public interface IXNet {

    void registerChannelType(IChannelType type);

    /**
     * Register a connectable implementation. You can use this instead of implementing IConnectable
     * on your block. The connectable interface will have the responsability of checking if it is
     * being called on the right block
     */
    void registerConnectable(@Nonnull IConnectable connectable);

    /**
     * Register a consumer provider. You can use this to provide additional consumers
     * (connectors) to controllers.
     */
    void registerConsumerProvider(@Nonnull IConsumerProvider consumerProvider);

    /**
     * Get the world blob for a given dimension
     */
    IWorldBlob getWorldBlob(World world);
}
