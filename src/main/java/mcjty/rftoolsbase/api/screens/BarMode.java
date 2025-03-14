package mcjty.rftoolsbase.api.screens;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum BarMode implements StringRepresentable {
    MODE_TEXT("Text"),
    MODE_PERTICK("PerTick"),
    MODE_PERCENTAGE("Percentage"),
    MODE_NONE("None");

    private final String name;

    public static final Codec<BarMode> CODEC = StringRepresentable.fromEnum(BarMode::values);
    public static final StreamCodec<FriendlyByteBuf, BarMode> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(BarMode.class);

    BarMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BarMode getMode(String name) {
        for (BarMode style : values()) {
            if (name.equals(style.getName())) {
                return style;
            }
        }
        return MODE_TEXT;
    }

    public boolean hideText() {
        return this == MODE_NONE;
    }

    public boolean showPerTick() {
        return this == MODE_PERTICK;
    }

    public boolean showPercentage() {
        return this == MODE_PERCENTAGE;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
