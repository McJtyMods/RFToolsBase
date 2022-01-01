package mcjty.rftoolsbase.api.machineinfo;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityMachineInformation {

    public static Capability<IMachineInformation> MACHINE_INFORMATION_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});;

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IMachineInformation.class);
    }
}
