package mcjty.rftoolsbase.api.screens;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum TextAlign implements StringRepresentable {
    ALIGN_LEFT,
    ALIGN_CENTER,
    ALIGN_RIGHT;

    public static final Codec<TextAlign> CODEC = StringRepresentable.fromEnum(TextAlign::values);
    public static final StreamCodec<FriendlyByteBuf, TextAlign> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(TextAlign.class);

    public static TextAlign get(String alignment) {
        if ("Left".equals(alignment)) {
            return TextAlign.ALIGN_LEFT;
        } else {
            return "Right".equals(alignment) ? TextAlign.ALIGN_RIGHT : TextAlign.ALIGN_CENTER;
        }
    }

    @Override
    public String getSerializedName() {
        return name();
    }
}
