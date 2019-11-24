package mcjty.rftoolsbase.api.xnet.channels;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implement this on your block if you want to be connectable to an XNet connector
 */
public interface IConnectable {

    /**
     * Return true if a connector can connect to this block. Warning! This has to work
     * both client and server side!
     * @param access
     * @param connectorPos the position of the connector (not your block!)
     * @param blockPos the position of your block
     * @param tileEntity a tile entity at this block position. Can be null if there is no tile entity here
     * @param facing the direction (as seen from the connector) towards your block
     */
    ConnectResult canConnect(@Nonnull IBlockReader access, @Nonnull BlockPos connectorPos, @Nonnull BlockPos blockPos,
                             @Nullable TileEntity tileEntity,
                             @Nonnull Direction facing);

    enum ConnectResult {
        NO,                 // No connection possible. Don't try further
        YES,                // Connection ok
        DEFAULT             // Don't know. Let XNet decide
    }
}
