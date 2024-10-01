package mcjty.rftoolsbase.modules.various.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record WrenchData(GlobalPos pos) {

    public static final Codec<WrenchData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GlobalPos.CODEC.fieldOf("pos").forGetter(WrenchData::pos)
    ).apply(instance, WrenchData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WrenchData> STREAM_CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, WrenchData::pos,
            WrenchData::new
    );

    public WrenchData withPos(GlobalPos pos) {
        return new WrenchData(pos);
    }
}
