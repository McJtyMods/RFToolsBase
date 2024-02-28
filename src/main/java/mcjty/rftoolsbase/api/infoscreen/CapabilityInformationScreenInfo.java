package mcjty.rftoolsbase.api.infoscreen;

import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityInformationScreenInfo {

    public static Capability<IInformationScreenInfo> INFORMATION_SCREEN_INFO_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});;

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IInformationScreenInfo.class);
    }
}
