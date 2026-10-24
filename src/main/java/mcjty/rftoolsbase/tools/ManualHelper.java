package mcjty.rftoolsbase.tools;

import mcjty.lib.gui.ManualEntry;
import net.minecraft.resources.Identifier;

public class ManualHelper {

    public static ManualEntry create(String entryName) {
        Identifier entry = Identifier.parse(entryName);
        return new ManualEntry(Identifier.parse("rftoolsbase:manual"), Identifier.fromNamespaceAndPath("rftoolsbase", entry.getPath()), 0);
    }
}
