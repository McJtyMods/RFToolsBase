package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.setup.DefaultModSetup;
import mcjty.rftoolsbase.api.infoscreen.CapabilityInformationScreenInfo;
import mcjty.rftoolsbase.api.machineinfo.CapabilityMachineInformation;
import mcjty.rftoolsbase.modules.hud.Hud;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
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
            McJtyLib.registerListCommandInfo(Hud.COMMAND_GETHUDLOG, String.class, buf -> buf.readUtf(32767), FriendlyByteBuf::writeUtf);
        });

        RFToolsBaseMessages.registerMessages("rftoolsbase");
        MinecraftForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent event) -> {
            if (!event.level.isClientSide) {
                TickOrderHandler.postWorldTick(event.level.dimension());
            }
        });
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        CapabilityInformationScreenInfo.register(event);
        CapabilityMachineInformation.register(event);
    }

    @Override
    protected void setupModCompat() {
    }
}
