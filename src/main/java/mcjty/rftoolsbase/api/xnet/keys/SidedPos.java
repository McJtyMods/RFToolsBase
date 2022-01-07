package mcjty.rftoolsbase.api.xnet.keys;

import mcjty.lib.varia.BlockPosTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record SidedPos(BlockPos pos, Direction side) implements Comparable<SidedPos> {

    @Override
    public String toString() {
        return "SidedPos{" + BlockPosTools.toString(pos) + "/" + side.getSerializedName() + "}";
    }

    @Override
    public int compareTo(SidedPos o) {
        int result = pos.compareTo(o.pos);
        if (result == 0) result = side.compareTo(o.side);
        return result;
    }
}
