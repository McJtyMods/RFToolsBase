package mcjty.rftoolsbase.api.machineinfo;

import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class CapabilityMachineInformation {

    public static final BlockCapability<IMachineInformation, @Nullable Direction> MACHINE_INFORMATION_CAPABILITY = BlockCapability.createSided(Identifier.fromNamespaceAndPath(RFToolsBase.MODID, "machine_information"), IMachineInformation.class);
}
