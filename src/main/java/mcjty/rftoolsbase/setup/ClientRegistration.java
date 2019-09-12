package mcjty.rftoolsbase.setup;


import mcjty.lib.gui.GenericGuiContainer;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.blocks.infuser.GuiMachineInfuser;
import mcjty.rftoolsbase.blocks.infuser.MachineInfuserSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent e) {
        GenericGuiContainer.register(MachineInfuserSetup.CONTAINER_INFUSER, GuiMachineInfuser::new);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    }
}
