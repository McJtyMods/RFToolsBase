package mcjty.rftoolsbase.api.xnet.keys;

import net.minecraft.core.Direction;

/**
 * A consumer ID and a side pointing towards the block
 * we are connecting too.
 */
public record SidedConsumer(ConsumerId consumerId, Direction side) {

    @Override
    public String toString() {
        return "SidedConsumer{id=" + consumerId + "/" + side + '}';
    }
}
