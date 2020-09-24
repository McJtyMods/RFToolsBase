package mcjty.rftoolsbase.setup;

import mcjty.lib.setup.DefaultModSetup;
import mcjty.lib.varia.DimensionId;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import mcjty.rftoolsbase.worldgen.OreGenerator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {


    public ModSetup() {
        createTab("rftoolsbase", () -> new ItemStack(VariousModule.SMARTWRENCH.get()));
    }

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);
        e.enqueueWork(() -> {
            OreGenerator.init();
            CommandHandler.registerCommands();
        });
        RFToolsBaseMessages.registerMessages("rftoolsbase");
        MinecraftForge.EVENT_BUS.addListener((TickEvent.WorldTickEvent event) -> {
            if (!event.world.isRemote) {
                TickOrderHandler.postWorldTick(DimensionId.fromWorld(event.world));
            }
        });
    }

    @Override
    protected void setupModCompat() {
    }
}
