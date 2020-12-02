package mcjty.rftoolsbase.api.xnet.helper;

import mcjty.rftoolsbase.api.xnet.channels.Color;
import mcjty.rftoolsbase.api.xnet.channels.RSMode;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BaseStringTranslators {

    private static Map<String, RSMode> rsModeMap;
    private static Map<String, Color> colorMap;

    @Nullable
    public static RSMode getRSMode(String mode) {
        if (rsModeMap == null) {
            rsModeMap = new HashMap<>();
            for (RSMode value : RSMode.values()) {
                rsModeMap.put(value.name(), value);
            }
        }
        return rsModeMap.get(mode);
    }

    @Nullable
    public static Color getColor(String color) {
        if (colorMap == null) {
            colorMap = new HashMap<>();
            for (Color value : Color.values()) {
                colorMap.put(value.name(), value);
            }
        }
        return colorMap.get(color);
    }

}
