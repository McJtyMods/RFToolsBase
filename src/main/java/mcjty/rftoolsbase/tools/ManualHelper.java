package mcjty.rftoolsbase.tools;

import mcjty.lib.gui.ManualEntry;
import net.minecraft.resources.ResourceLocation;

public class ManualHelper {

    public static ManualEntry create(String entryName) {
        return new ManualEntry(ResourceLocation.parse("rftoolsbase:manual"), ResourceLocation.parse(entryName), 0);
    }
}
