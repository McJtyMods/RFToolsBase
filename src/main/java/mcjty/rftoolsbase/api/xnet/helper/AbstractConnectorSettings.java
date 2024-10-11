package mcjty.rftoolsbase.api.xnet.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import mcjty.lib.varia.OrientationTools;
import mcjty.rftoolsbase.api.xnet.channels.Color;
import mcjty.rftoolsbase.api.xnet.channels.IConnectorSettings;
import mcjty.rftoolsbase.api.xnet.channels.RSMode;
import mcjty.rftoolsbase.api.xnet.gui.IEditorGui;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractConnectorSettings implements IConnectorSettings {

    public static final String TAG_RS = "rs";
    public static final String TAG_COLOR = "color";
    public static final String TAG_FACING = "facing";

    private RSMode rsMode = RSMode.IGNORED;
    private int prevPulse = 0;
    private Color[] colors = new Color[]{Color.OFF, Color.OFF, Color.OFF, Color.OFF};
    private int colorsMask = 0;

    // Cache for advanced mode
    protected boolean advanced = false;

    @Nonnull
    private final Direction side;
    @Nullable
    private Direction facingOverride = null; // Only available on advanced connectors

    public AbstractConnectorSettings(@Nonnull Direction side) {
        this.side = side;
    }

    @Nonnull
    public Direction getSide() {
        return side;
    }

    @Nonnull
    public Direction getFacing() {
        return facingOverride == null ? side : facingOverride;
    }

    public RSMode getRsMode() {
        return rsMode;
    }

    public int getPrevPulse() {
        return prevPulse;
    }

    public void setPrevPulse(int prevPulse) {
        this.prevPulse = prevPulse;
    }

    public Color[] getColors() {
        return colors;
    }

    private void calculateColorsMask() {
        colorsMask = 0;
        for (Color color : colors) {
            if (color != null && color != Color.OFF) {
                colorsMask |= 1 << color.ordinal();
            }
        }
    }

    public int getColorsMask() {
        return colorsMask;
    }

    @Override
    public void update(Map<String, Object> data) {
        if (data.containsKey(TAG_RS)) {
            rsMode = RSMode.valueOf(((String) data.get(TAG_RS)).toUpperCase());
        } else {
            rsMode = RSMode.IGNORED;
        }
        if (data.containsKey(TAG_COLOR + "0")) {
            colors[0] = Color.colorByValue((Integer) data.get(TAG_COLOR + "0"));
        } else {
            colors[0] = Color.OFF;
        }
        if (data.containsKey(TAG_COLOR + "1")) {
            colors[1] = Color.colorByValue((Integer) data.get(TAG_COLOR + "1"));
        } else {
            colors[1] = Color.OFF;
        }
        if (data.containsKey(TAG_COLOR + "2")) {
            colors[2] = Color.colorByValue((Integer) data.get(TAG_COLOR + "2"));
        } else {
            colors[2] = Color.OFF;
        }
        if (data.containsKey(TAG_COLOR + "3")) {
            colors[3] = Color.colorByValue((Integer) data.get(TAG_COLOR + "3"));
        } else {
            colors[3] = Color.OFF;
        }
        calculateColorsMask();
        String facing = (String) data.get(TAG_FACING);
        facingOverride = facing == null ? null : Direction.byName(facing.toLowerCase());
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
        setEnumSafe(object, "rsmode", rsMode);
        setEnumSafe(object, "color0", colors[0]);
        setEnumSafe(object, "color1", colors[1]);
        setEnumSafe(object, "color2", colors[2]);
        setEnumSafe(object, "color3", colors[3]);
        setEnumSafe(object, "side", side);   // Informative, isn't used to load
        setEnumSafe(object, "facingoverride", facingOverride);
        object.add("advancedneeded", new JsonPrimitive(false));
    }

    protected void readFromJsonInternal(JsonObject object) {
        rsMode = getEnumSafe(object, "rsmode", BaseStringTranslators::getRSMode);
        colors[0] = getEnumSafe(object, "color0", BaseStringTranslators::getColor);
        colors[1] = getEnumSafe(object, "color1", BaseStringTranslators::getColor);
        colors[2] = getEnumSafe(object, "color2", BaseStringTranslators::getColor);
        colors[3] = getEnumSafe(object, "color3", BaseStringTranslators::getColor);
        facingOverride = getEnumSafe(object, "facingoverride", s -> Direction.byName(s.toLowerCase()));
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        rsMode = RSMode.values()[tag.getByte("rsMode")];
        prevPulse = tag.getInt("prevPulse");
        colors[0] = Color.values()[tag.getByte("color0")];
        colors[1] = Color.values()[tag.getByte("color1")];
        colors[2] = Color.values()[tag.getByte("color2")];
        colors[3] = Color.values()[tag.getByte("color3")];
        calculateColorsMask();
        if (tag.contains("facingOverride")) {
            facingOverride = OrientationTools.DIRECTION_VALUES[tag.getByte("facingOverride")];
        }
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        tag.putByte("rsMode", (byte) rsMode.ordinal());
        tag.putInt("prevPulse", prevPulse);
        tag.putByte("color0", (byte) colors[0].ordinal());
        tag.putByte("color1", (byte) colors[1].ordinal());
        tag.putByte("color2", (byte) colors[2].ordinal());
        tag.putByte("color3", (byte) colors[3].ordinal());
        if (facingOverride != null) {
            tag.putByte("facingOverride", (byte) facingOverride.ordinal());
        }
    }

    protected IEditorGui sideGui(IEditorGui gui) {
        return gui.choices(TAG_FACING, "Side from which to operate", facingOverride == null ? side : facingOverride, OrientationTools.DIRECTION_VALUES);
    }

    protected IEditorGui colorsGui(IEditorGui gui) {
        return gui
                .colors(TAG_COLOR + "0", "Enable on color", colors[0].getColor(), Color.COLORS)
                .colors(TAG_COLOR + "1", "Enable on color", colors[1].getColor(), Color.COLORS)
                .colors(TAG_COLOR + "2", "Enable on color", colors[2].getColor(), Color.COLORS)
                .colors(TAG_COLOR + "3", "Enable on color", colors[3].getColor(), Color.COLORS);
    }

    protected IEditorGui redstoneGui(IEditorGui gui) {
        return gui.redstoneMode(TAG_RS, rsMode);
    }
}
