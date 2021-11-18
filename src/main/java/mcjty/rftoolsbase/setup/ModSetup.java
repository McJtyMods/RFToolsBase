package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.setup.DefaultModSetup;
import mcjty.rftoolsbase.api.infoscreen.CapabilityInformationScreenInfo;
import mcjty.rftoolsbase.api.machineinfo.CapabilityMachineInformation;
import mcjty.rftoolsbase.modules.hud.Hud;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import mcjty.rftoolsbase.worldgen.OreGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
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
            CommandHandler.registerCommands();
            // Needs to be here: after registration of everything and after reading config
            OreGenerator.registerConfiguredFeatures();
            McJtyLib.registerCommandInfo(Hud.COMMAND_GETHUDLOG, String.class, buf -> buf.readUtf(32767), PacketBuffer::writeUtf);
        });

        CapabilityMachineInformation.register();
        CapabilityInformationScreenInfo.register();

        RFToolsBaseMessages.registerMessages("rftoolsbase");
        MinecraftForge.EVENT_BUS.addListener((TickEvent.WorldTickEvent event) -> {
            if (!event.world.isClientSide) {
                TickOrderHandler.postWorldTick(event.world.dimension());
            }
        });
    }

    @Override
    protected void setupModCompat() {
    }
}
