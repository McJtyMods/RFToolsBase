package mcjty.rftoolsbase.setup;

import mcjty.lib.setup.DefaultClientProxy;
import mcjty.rftoolsbase.blocks.infuser.MachineInfuserSetup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@SuppressWarnings("EmptyClass")
public class ClientProxy extends DefaultClientProxy {

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);

        MachineInfuserSetup.initClient();
    }
}
