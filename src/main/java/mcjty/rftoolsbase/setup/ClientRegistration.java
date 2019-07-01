package mcjty.rftoolsbase.setup;


import mcjty.lib.McJtyLib;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.blocks.infuser.GuiMachineInfuser;
import mcjty.rftoolsbase.blocks.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.blocks.infuser.MachineInfuserTileEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent e) {
        ScreenManager.IScreenFactory<GenericContainer, GuiMachineInfuser> factory = (container, inventory, title) -> {
            TileEntity te = McJtyLib.proxy.getClientWorld().getTileEntity(container.getPos());
            return Tools.safeMap(te, (MachineInfuserTileEntity i) -> new GuiMachineInfuser(i, container, inventory), "Invalid tile entity!");
        };
        ScreenManager.registerFactory(MachineInfuserSetup.CONTAINER_INFUSER, factory);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    }
}
