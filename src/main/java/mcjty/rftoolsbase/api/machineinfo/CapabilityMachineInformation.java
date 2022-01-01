package mcjty.rftoolsbase.api.machineinfo;

import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMachineInformation {

    @CapabilityInject(IMachineInformation.class)
    public static Capability<IMachineInformation> MACHINE_INFORMATION_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IMachineInformation.class, new Capability.IStorage<IMachineInformation>() {
            @Override
            public Tag writeNBT(Capability<IMachineInformation> capability, IMachineInformation instance, Direction side) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void readNBT(Capability<IMachineInformation> capability, IMachineInformation instance, Direction side, Tag nbt) {
                throw new UnsupportedOperationException();
            }
        }, () -> {
            throw new UnsupportedOperationException();
        });
    }

}
