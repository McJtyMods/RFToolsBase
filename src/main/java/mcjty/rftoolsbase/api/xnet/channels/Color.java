package mcjty.rftoolsbase.api.xnet.channels;

import mcjty.rftoolsbase.api.teleportation.ITeleportationManager;

import java.util.HashMap;
import java.util.Map;

public enum Color {
    OFF(0x000000),
    WHITE(0xffffff),
    RED(0xff0000),
    GREEN(0x00ff00),
    BLUE(0x0000ff),
    YELLOW(0xffff00),
    CYAN(0x00ffff),
    PURPLE(0xff00ff),
    ORANGE(0xff8800),
    GRAY(0x888888),
    DARK_RED(0x880000),
    DARK_GREEN(0x008800),
    DARK_BLUE(0x000088),
    DARK_YELLOW(0x888800),
    DARK_CYAN(0x008888),
    DARK_PURPLE(0x880088);

    private final int color;

    Color(int color) {
        this.color = color;
    }

    private static final Map<Integer, Color> COLOR_MAP = new HashMap<>();
    public static final Integer[] COLORS = new Integer[Color.values().length];

    static {
        for (int i = 0; i < Color.values().length; i++) {
            Color col = Color.values()[i];
            COLORS[i] = col.color;
            COLOR_MAP.put(col.color, col);
        }
    }

    public int getColor() {
        return color;
    }

    public static Color colorByValue(int color) {
        return COLOR_MAP.get(color);
    }
}
