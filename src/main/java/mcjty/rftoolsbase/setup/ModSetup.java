package mcjty.rftoolsbase.setup;

import mcjty.lib.setup.DefaultModSetup;
import mcjty.rftoolsbase.api.machineinfo.CapabilityMachineInformation;
import mcjty.rftoolsbase.items.ModItems;
import mcjty.rftoolsbase.network.RFToolsBaseMessages;
import mcjty.rftoolsbase.worldgen.OreGenerator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    public ModSetup() {
        createTab("rftoolsbase", () -> new ItemStack(ModItems.SMARTWRENCH));
    }

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);
        OreGenerator.init();
        CapabilityMachineInformation.register();
        CommandHandler.registerCommands();
        RFToolsBaseMessages.registerMessages("rftoolsbase");
    }

    @Override
    protected void setupModCompat() { }
}
