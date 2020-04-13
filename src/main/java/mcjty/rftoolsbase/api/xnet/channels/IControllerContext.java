package mcjty.rftoolsbase.api.xnet.channels;

import mcjty.rftoolsbase.api.xnet.keys.ConsumerId;
import mcjty.rftoolsbase.api.xnet.keys.NetworkId;
import mcjty.rftoolsbase.api.xnet.keys.SidedConsumer;
import mcjty.rftoolsbase.api.xnet.keys.SidedPos;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The controller tile entity implements this interface
 */
public interface IControllerContext {

    World getControllerWorld();

    NetworkId getNetworkId();

    @Nullable
    BlockPos findConsumerPosition(@Nonnull ConsumerId consumerId);

    @Nonnull
    Map<SidedConsumer, IConnectorSettings> getConnectors(int channel);

    @Nonnull
    Map<SidedConsumer, IConnectorSettings> getRoutedConnectors(int channel);

    boolean matchColor(int colorMask);

    boolean checkAndConsumeRF(int rft);

    /**
     * Get a filter by index
     */
    @Nonnull
    Predicate<ItemStack> getIndexedFilter(int idx);

    /*
     * This allows other mods to iterate over all blocks the controller is
     * connected to without worrying about configured channels. With this
     * they can basically piggy-back onto the existing XNet network.
     * Note that this does also contain all connected blocks that are connected
     * through IConsumerProviders
     */
    List<SidedPos> getConnectedBlockPositions();

}
