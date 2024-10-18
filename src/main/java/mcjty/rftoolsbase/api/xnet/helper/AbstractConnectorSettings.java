package mcjty.rftoolsbase.api.xnet.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcjty.lib.varia.OrientationTools;
import mcjty.rftoolsbase.api.xnet.channels.Color;
import mcjty.rftoolsbase.api.xnet.channels.IConnectorSettings;
import mcjty.rftoolsbase.api.xnet.channels.RSMode;
import mcjty.rftoolsbase.api.xnet.gui.IEditorGui;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractConnectorSettings implements IConnectorSettings {

    public static final String TAG_RS = "rs";
    public static final String TAG_COLOR = "color";
    public static final String TAG_FACING = "facing";

    public static final BaseSettings DEFAULT_SETTINGS = new BaseSettings(RSMode.IGNORED, Color.OFF, Color.OFF, Color.OFF, Color.OFF, null);
    protected BaseSettings settings;
    private int colorsMask = 0;
    private int prevPulse = 0;

    // Cache for advanced mode
    protected boolean advanced = false;
    @Nonnull
    private final Direction side;

    public record BaseSettings(RSMode rsMode, Color color0, Color color1, Color color2, Color color3, Direction facingOverride) {

        public static final Codec<BaseSettings> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        RSMode.CODEC.fieldOf("rsMode").forGetter(BaseSettings::rsMode),
                        Color.CODEC.fieldOf("color0").forGetter(BaseSettings::color0),
                        Color.CODEC.fieldOf("color1").forGetter(BaseSettings::color1),
                        Color.CODEC.fieldOf("color2").forGetter(BaseSettings::color2),
                        Color.CODEC.fieldOf("color3").forGetter(BaseSettings::color3),
                        Direction.CODEC.optionalFieldOf("facingOverride").forGetter(s -> Optional.ofNullable(s.facingOverride()))
                ).apply(instance, (rsMode, c0, c1, c2, c3, direction) -> new BaseSettings(rsMode, c0, c1, c2, c3, direction.orElse(null))));

        public static final StreamCodec<FriendlyByteBuf, BaseSettings> STREAM_CODEC = StreamCodec.composite(
                RSMode.STREAM_CODEC, BaseSettings::rsMode,
                Color.STREAM_CODEC, BaseSettings::color0,
                Color.STREAM_CODEC, BaseSettings::color1,
                Color.STREAM_CODEC, BaseSettings::color2,
                Color.STREAM_CODEC, BaseSettings::color3,
                ByteBufCodecs.optional(Direction.STREAM_CODEC), s -> Optional.ofNullable(s.facingOverride()),
                (rsMode, c0, c1, c2, c3, direction) -> new BaseSettings(rsMode, c0, c1, c2, c3, direction.orElse(null)));
    }

    public AbstractConnectorSettings(@Nonnull BaseSettings base, @Nonnull Direction side) {
        this.settings = base;
        this.side = side;
        calculateColorsMask();
    }

    @Nonnull
    public Direction getSide() {
        return side;
    }

    @Nonnull
    public Direction getFacing() {
        return settings.facingOverride == null ? side : settings.facingOverride;
    }

    public RSMode getRsMode() {
        return settings.rsMode;
    }

    public int getPrevPulse() {
        return prevPulse;
    }

    public void setPrevPulse(int prevPulse) {
        this.prevPulse = prevPulse;
    }

    private void calculateColorsMask() {
        colorsMask = 0;
        if (settings.color0 != Color.OFF) {
            colorsMask |= 1 << settings.color0.ordinal();
        }
        if (settings.color1 != Color.OFF) {
            colorsMask |= 1 << settings.color1.ordinal();
        }
        if (settings.color2 != Color.OFF) {
            colorsMask |= 1 << settings.color2.ordinal();
        }
        if (settings.color3 != Color.OFF) {
            colorsMask |= 1 << settings.color3.ordinal();
        }
    }

    public int getColorsMask() {
        return colorsMask;
    }

    @Override
    public void update(Map<String, Object> data) {
        RSMode rsMode;
        if (data.containsKey(TAG_RS)) {
            rsMode = RSMode.valueOf(((String) data.get(TAG_RS)).toUpperCase());
        } else {
            rsMode = RSMode.IGNORED;
        }
        Color colors0;
        Color colors1;
        Color colors2;
        Color colors3;
        if (data.containsKey(TAG_COLOR + "0")) {
            colors0 = Color.colorByValue((Integer) data.get(TAG_COLOR + "0"));
        } else {
            colors0 = Color.OFF;
        }
        if (data.containsKey(TAG_COLOR + "1")) {
            colors1 = Color.colorByValue((Integer) data.get(TAG_COLOR + "1"));
        } else {
            colors1 = Color.OFF;
        }
        if (data.containsKey(TAG_COLOR + "2")) {
            colors2 = Color.colorByValue((Integer) data.get(TAG_COLOR + "2"));
        } else {
            colors2 = Color.OFF;
        }
        if (data.containsKey(TAG_COLOR + "3")) {
            colors3 = Color.colorByValue((Integer) data.get(TAG_COLOR + "3"));
        } else {
            colors3 = Color.OFF;
        }
        String facing = (String) data.get(TAG_FACING);
        Direction facingOverride = facing == null ? null : Direction.byName(facing.toLowerCase());
        settings = new BaseSettings(rsMode, colors0, colors1, colors2, colors3, facingOverride);
        calculateColorsMask();
    }

    protected static <T extends Enum<T>> void setEnumSafe(JsonObject object, String tag, T value) {
        if (value != null) {
            object.add(tag, new JsonPrimitive(value.name()));
        }
    }

    protected static <T extends Enum<T>> T getEnumSafe(JsonObject object, String tag, Function<String, T> translator) {
        if (object.has(tag)) {
            return translator.apply(object.get(tag).getAsString());
        } else {
            return null;
        }
    }

    protected static void setIntegerSafe(JsonObject object, String tag, Integer value) {
        if (value != null) {
            object.add(tag, new JsonPrimitive(value));
        }
    }

    protected static Integer getIntegerSafe(JsonObject object, String tag) {
        if (object.has(tag)) {
            return object.get(tag).getAsInt();
        } else {
            return null;
        }
    }

    protected static int getIntegerNotNull(JsonObject object, String tag) {
        if (object.has(tag)) {
            return object.get(tag).getAsInt();
        } else {
            return 0;
        }
    }

    protected static boolean getBoolSafe(JsonObject object, String tag) {
        if (object.has(tag)) {
            return object.get(tag).getAsBoolean();
        } else {
            return false;
        }
    }

    protected void writeToJsonInternal(JsonObject object) {
        setEnumSafe(object, "rsmode", settings.rsMode);
        setEnumSafe(object, "color0", settings.color0);
        setEnumSafe(object, "color1", settings.color1);
        setEnumSafe(object, "color2", settings.color2);
        setEnumSafe(object, "color3", settings.color3);
        setEnumSafe(object, "side", side);   // Informative, isn't used to load
        setEnumSafe(object, "facingoverride", settings.facingOverride);
        object.add("advancedneeded", new JsonPrimitive(false));
    }

    protected void readFromJsonInternal(JsonObject object) {
        RSMode rsMode = getEnumSafe(object, "rsmode", BaseStringTranslators::getRSMode);
        Color color0 = getEnumSafe(object, "color0", BaseStringTranslators::getColor);
        Color color1 = getEnumSafe(object, "color1", BaseStringTranslators::getColor);
        Color color2 = getEnumSafe(object, "color2", BaseStringTranslators::getColor);
        Color color3 = getEnumSafe(object, "color3", BaseStringTranslators::getColor);
        Direction facingOverride = getEnumSafe(object, "facingoverride", s -> Direction.byName(s.toLowerCase()));
        settings = new BaseSettings(rsMode, color0, color1, color2, color3, facingOverride);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        prevPulse = tag.getInt("prevPulse");
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        tag.putInt("prevPulse", prevPulse);
    }

    protected IEditorGui sideGui(IEditorGui gui) {
        return gui.choices(TAG_FACING, "Side from which to operate", settings.facingOverride == null ? side : settings.facingOverride, OrientationTools.DIRECTION_VALUES);
    }

    protected IEditorGui colorsGui(IEditorGui gui) {
        return gui
                .colors(TAG_COLOR + "0", "Enable on color", settings.color0.getColor(), Color.COLORS)
                .colors(TAG_COLOR + "1", "Enable on color", settings.color1.getColor(), Color.COLORS)
                .colors(TAG_COLOR + "2", "Enable on color", settings.color2.getColor(), Color.COLORS)
                .colors(TAG_COLOR + "3", "Enable on color", settings.color3.getColor(), Color.COLORS);
    }

    protected IEditorGui redstoneGui(IEditorGui gui) {
        return gui.redstoneMode(TAG_RS, settings.rsMode);
    }
}
