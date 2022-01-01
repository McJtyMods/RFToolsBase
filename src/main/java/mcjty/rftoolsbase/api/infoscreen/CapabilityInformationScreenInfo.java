package mcjty.rftoolsbase.api.infoscreen;

import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityInformationScreenInfo {

    @CapabilityInject(IInformationScreenInfo.class)
    public static Capability<IInformationScreenInfo> INFORMATION_SCREEN_INFO_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IInformationScreenInfo.class, new Capability.IStorage<IInformationScreenInfo>() {
            @Override
            public Tag writeNBT(Capability<IInformationScreenInfo> capability, IInformationScreenInfo instance, Direction side) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void readNBT(Capability<IInformationScreenInfo> capability, IInformationScreenInfo instance, Direction side, Tag nbt) {
                throw new UnsupportedOperationException();
            }
        }, () -> {
            throw new UnsupportedOperationException();
        });
    }

}
