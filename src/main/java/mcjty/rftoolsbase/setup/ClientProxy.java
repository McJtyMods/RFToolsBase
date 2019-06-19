package mcjty.rftoolsbase.setup;

import mcjty.lib.container.GenericContainer;
import mcjty.lib.setup.DefaultClientProxy;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.blocks.infuser.GuiMachineInfuser;
import mcjty.rftoolsbase.blocks.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.blocks.infuser.MachineInfuserTileEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy extends DefaultClientProxy {

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);

        MachineInfuserSetup.initClient();
        initGuis();
    }

    public static void initGuis() {
        ScreenManager.IScreenFactory<GenericContainer, GuiMachineInfuser> factory = (container, inventory, title) -> {
            BlockPos pos = container.getPos();
            TileEntity te = RFToolsBase.proxy.getClientWorld().getTileEntity(pos);
            if (te instanceof MachineInfuserTileEntity) {
                return new GuiMachineInfuser((MachineInfuserTileEntity) te, container, inventory);
            } else {
                throw new IllegalStateException("Where is my infuser?");
            }
        };
        ScreenManager.registerFactory(MachineInfuserSetup.MACHINE_INFUSER_CONTAINER, factory);
    }
}
