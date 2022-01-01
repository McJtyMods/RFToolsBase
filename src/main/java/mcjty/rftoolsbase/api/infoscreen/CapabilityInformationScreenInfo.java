package mcjty.rftoolsbase.api.infoscreen;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityInformationScreenInfo {

    public static Capability<IInformationScreenInfo> INFORMATION_SCREEN_INFO_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});;

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IInformationScreenInfo.class);
    }
}
