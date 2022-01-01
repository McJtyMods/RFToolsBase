package mcjty.rftoolsbase.api.client;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.List;

/**
 * Implement in a TE
 */
public interface IHudSupport {

    Direction getBlockOrientation();

    boolean isBlockAboveAir();

    List<String> getClientLog();

    long getLastUpdateTime();

    void setLastUpdateTime(long t);

    BlockPos getHudPos();
}
