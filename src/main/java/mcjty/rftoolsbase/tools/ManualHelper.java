package mcjty.rftoolsbase.tools;

import mcjty.lib.gui.ManualEntry;
import net.minecraft.resources.ResourceLocation;

public class ManualHelper {

    public static ManualEntry create(String entryName) {
        ResourceLocation entry = new ResourceLocation(entryName);
        return new ManualEntry(new ResourceLocation("rftoolsbase:manual"), new ResourceLocation("rftoolsbase", entry.getPath()), 0);
    }
}
