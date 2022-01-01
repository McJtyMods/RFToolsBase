package mcjty.rftoolsbase.api.xnet.channels;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IChannelType {

    String getID();

    String getName();

    /**
     * Return true if the block at that specific side (can be null) supports this channel type
     */
    boolean supportsBlock(@Nonnull Level world, @Nonnull BlockPos pos, @Nullable Direction side);

    /**
     * The 'advanced' parameter is no longer used and will always be 'false'. Don't depend on this
     */
    @Nonnull
    IConnectorSettings createConnector(@Nonnull Direction side);

    @Nonnull
    IChannelSettings createChannel();
}
