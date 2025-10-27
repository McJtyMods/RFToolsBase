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
 * This class identifies an inventory on a network. It has an optional
 * node name. If that is not given then the processor itself is meant.
 * There is also a side adjacent to the node or processor and an
 * optional internal side. The internal side represents from which side
 * we are supposedly accessing the inventory.
 */
public class Inventory extends BlockSide {

    @Nullable private final Direction intSide;   // The side at which we are accessing the inventory (can be null)

    public static final Codec<Inventory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("node").forGetter(inv -> Optional.ofNullable(inv.getNodeName())),
            Direction.CODEC.fieldOf("side").forGetter(Inventory::getSide),
            Direction.CODEC.optionalFieldOf("int_side").forGetter(inv -> Optional.ofNullable(inv.getIntSide()))
    ).apply(instance, (node, side, intSide) -> new Inventory(node.orElse(null), side, intSide.orElse(null))));

    public static final StreamCodec<RegistryFriendlyByteBuf, Inventory> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), inv -> Optional.ofNullable(inv.getNodeName()),
            Direction.STREAM_CODEC, Inventory::getSide,
            ByteBufCodecs.optional(Direction.STREAM_CODEC), inv -> Optional.ofNullable(inv.intSide),
            (node, side, intSide) -> new Inventory(node.orElse(null), side, intSide.orElse(null))
    );

    public Inventory(@Nullable String name, @Nonnull Direction side, @Nullable Direction intSide) {
        super(name, side);
        this.intSide = intSide;
    }

    public String serialize() {
        return "#" + (hasNodeName() ? getNodeName() : "-") + "#" + getSide().getSerializedName() + "#" + (intSide == null ? "-" : intSide.getSerializedName()) + "#";
    }

    public static Inventory deserialize(String s) {
        String[] splitted = StringUtils.split(s, '#');
        return new Inventory("-".equals(splitted[0]) ? null : splitted[0], Direction.byName(splitted[1]),
                "-".equals(splitted[2]) ? null : Direction.byName(splitted[2]));
    }

    @Override
    @Nonnull
    public Direction getSide() {
        return super.getSide();
    }

    @Nullable
    public Direction getIntSide() {
        return intSide;
    }

    @Override
    public String getStringRepresentation() {
        String s = StringUtils.left(getSide().getSerializedName().toUpperCase(), 1);
        if (getIntSide() == null) {
            s += "/*";
        } else {
            String is = StringUtils.left(getIntSide().getSerializedName().toUpperCase(), 1);
            s += "/" + is;
        }
        if (getNodeName() == null) {
            return s;
        } else {
            return StringUtils.left(getNodeName(), 6) + " " + s;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Inventory inventory = (Inventory) o;

        if (intSide != inventory.intSide) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (intSide != null ? intSide.hashCode() : 0);
        return result;
    }
}
