package mcjty.rftoolsbase.api.machineinfo;

import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityMachineInformation {

    public static Capability<IMachineInformation> MACHINE_INFORMATION_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});;

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IMachineInformation.class);
    }
}
