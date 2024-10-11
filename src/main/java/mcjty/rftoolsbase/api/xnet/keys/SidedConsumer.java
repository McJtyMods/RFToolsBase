package mcjty.rftoolsbase.api.xnet.keys;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * A consumer ID and a side pointing towards the block
 * we are connecting too.
 */
public record SidedConsumer(ConsumerId consumerId, Direction side) {

    public static final Codec<SidedConsumer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(sidedConsumer -> sidedConsumer.consumerId.id()),
            Direction.CODEC.fieldOf("side").forGetter(SidedConsumer::side)
    ).apply(instance, (id, direction) -> new SidedConsumer(new ConsumerId(id), direction)));

    public static final StreamCodec<ByteBuf, SidedConsumer> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, sidedConsumer -> sidedConsumer.consumerId.id(),
            Direction.STREAM_CODEC, SidedConsumer::side,
            (id, direction) -> new SidedConsumer(new ConsumerId(id), direction)
    );

    @Override
    public String toString() {
        return "SidedConsumer{id=" + consumerId + "/" + side + '}';
    }
}
