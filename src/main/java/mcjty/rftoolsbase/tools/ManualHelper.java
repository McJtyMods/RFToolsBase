package mcjty.rftoolsbase.tools;

import mcjty.lib.gui.ManualEntry;
import net.minecraft.util.ResourceLocation;

public class ManualHelper {

    public static ManualEntry create(String entryName) {
        return new ManualEntry(new ResourceLocation("rftoolsbase:manual"), new ResourceLocation(entryName), 0);
    }
}
