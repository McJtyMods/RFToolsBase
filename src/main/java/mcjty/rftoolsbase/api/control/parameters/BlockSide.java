package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * This class identifies a side of a network blocked. This basically
 * is an optional nodename and an optional side. If side is null then
 * it means the node or processor itself.
 */
public class BlockSide implements Comparable<BlockSide> {
    @Nullable private final String nodeName;          // An inventory on a network
    @Nullable private final Direction side;      // The side at which the inventory can be found

    private static final int MAX_STRING_LENGTH = 32767;

    public static final Codec<BlockSide> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("node").forGetter(side -> Optional.ofNullable(side.getNodeName())),
            Direction.CODEC.optionalFieldOf("side").forGetter(side -> Optional.ofNullable(side.getSide()))
    ).apply(instance, (node, dir) -> new BlockSide(node.orElse(null), dir.orElse(null))));

    public static final StreamCodec<RegistryFriendlyByteBuf, BlockSide> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), inv -> Optional.ofNullable(inv.getNodeName()),
            ByteBufCodecs.optional(Direction.STREAM_CODEC), inv -> Optional.ofNullable(inv.getSide()),
            (node, side) -> new BlockSide(node.orElse(null), side.orElse(null))
    );

    public BlockSide(@Nullable String name, @Nullable Direction side) {
        this.nodeName = (name == null || name.isEmpty()) ? null : name;
        this.side = side;
    }

    @Override
    public int compareTo(@Nonnull BlockSide blockSide) {
        if (nodeName == null && blockSide.nodeName != null) {
            return -1;
        }
        if (nodeName != null && blockSide.nodeName == null) {
            return 1;
        }
        if (nodeName == null) {
            return 0;
        }
        return nodeName.compareTo(blockSide.nodeName);
    }

    @Nullable
    public String getNodeName() {
        return nodeName;
    }

    @Nonnull
    public String getNodeNameSafe() {
        return nodeName == null ? "" : nodeName;
    }

    public boolean hasNodeName() {
        return nodeName != null && !nodeName.isEmpty();
    }

    @Nullable
    public Direction getSide() {
        return side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlockSide blockSide = (BlockSide) o;

        if (nodeName != null ? !nodeName.equals(blockSide.nodeName) : blockSide.nodeName != null) {
            return false;
        }
        if (side != blockSide.side) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = nodeName != null ? nodeName.hashCode() : 0;
        result = 31 * result + (side != null ? side.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (side == null) {
            return "*";
        } else {
            return side.toString();
        }
    }

    public String getStringRepresentation() {
        Direction facing = getSide();

        String s = facing == null ? "" : StringUtils.left(facing.getSerializedName().toUpperCase(), 1);
        if (getNodeName() == null) {
            return s;
        } else {
            return StringUtils.left(getNodeName(), 7) + " " + s;
        }
    }

}
