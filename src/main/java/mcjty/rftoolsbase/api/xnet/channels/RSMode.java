package mcjty.rftoolsbase.api.xnet.channels;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import mcjty.lib.network.StreamCodecs;

public enum RSMode implements StringRepresentable {
    IGNORED,
    OFF,
    ON,
    PULSE;

    public static final Codec<RSMode> CODEC = StringRepresentable.fromEnum(RSMode::values);
    public static final StreamCodec<FriendlyByteBuf, RSMode> STREAM_CODEC = StreamCodecs.enumCodec(RSMode.class);

    @Override
    public String getSerializedName() {
        return name();
    }
}
