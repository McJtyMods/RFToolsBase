package mcjty.rftoolsbase.api.screens;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum FormatStyle implements StringRepresentable {
    MODE_FULL("Full"),
    MODE_COMPACT("Compact"),
    MODE_COMMAS("Commas");

    private final String name;

    public static final Codec<FormatStyle> CODEC = StringRepresentable.fromEnum(FormatStyle::values);
    public static final StreamCodec<FriendlyByteBuf, FormatStyle> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(FormatStyle.class);

    FormatStyle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FormatStyle getStyle(String name) {
        for (FormatStyle style : values()) {
            if (name.equals(style.getName())) {
                return style;
            }
        }
        return MODE_FULL;
    }


    @Override
    public String getSerializedName() {
        return name;
    }
}
