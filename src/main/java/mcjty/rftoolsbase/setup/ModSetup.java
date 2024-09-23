package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.setup.DefaultModSetup;
import mcjty.rftoolsbase.modules.hud.Hud;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class ModSetup extends DefaultModSetup {

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);
        e.enqueueWork(() -> {
            CommandHandler.registerCommands();
            // Needs to be here: after registration of everything and after reading config
            McJtyLib.registerListCommandInfo(Hud.COMMAND_GETHUDLOG, String.class, buf -> buf.readUtf(32767), FriendlyByteBuf::writeUtf);
        });

        NeoForge.EVENT_BUS.addListener((LevelTickEvent event) -> {
            if (!event.getLevel().isClientSide) {
                TickOrderHandler.postWorldTick(event.getLevel().dimension());
            }
        });
    }

    // @todo 1.21
//    public void registerCapabilities(RegisterCapabilitiesEvent event) {
//        CapabilityInformationScreenInfo.register(event);
//        CapabilityMachineInformation.register(event);
//    }

    @Override
    protected void setupModCompat() {
    }
}
