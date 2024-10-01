package mcjty.rftoolsbase.modules.infuser.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record InfuserData(int infusing) {

    public static final Codec<InfuserData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("infusing").forGetter(InfuserData::infusing)
    ).apply(instance, InfuserData::new));

    public static final InfuserData EMPTY = new InfuserData(0);

    public static final StreamCodec<RegistryFriendlyByteBuf, InfuserData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, InfuserData::infusing,
            InfuserData::new
    );
}
