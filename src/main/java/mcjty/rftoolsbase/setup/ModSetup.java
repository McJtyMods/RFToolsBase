package mcjty.rftoolsbase.setup;

import mcjty.lib.setup.DefaultModSetup;
import mcjty.rftoolsbase.items.ModItems;
import net.minecraft.item.ItemStack;

public class ModSetup extends DefaultModSetup {

    public ModSetup() {
        createTab("rftoolsbase", () -> new ItemStack(ModItems.SMARTWRENCH));
    }

    @Override
    protected void setupModCompat() { }

    @Override
    protected void setupConfig() {

    }
}
