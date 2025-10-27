package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Tuple(int x, int y) implements Comparable<Tuple> {

    public static final Codec<Tuple> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Tuple::x),
            Codec.INT.fieldOf("y").forGetter(Tuple::y)
    ).apply(instance, Tuple::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Tuple> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Tuple::x,
            ByteBufCodecs.INT, Tuple::y,
            Tuple::new);

    @Override
    public int compareTo(Tuple tuple) {
        if (x < tuple.x) {
            return -1;
        } else if (x > tuple.x) {
            return 1;
        } else {
            if (y < tuple.y) {
                return -1;
            } else if (y > tuple.y) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
