package mcjty.rftoolsbase.api.machineinfo;

import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;

public class CapabilityMachineInformation {

    public static final BlockApiLookup<IMachineInformation, @Nullable Direction> MACHINE_INFORMATION_CAPABILITY = BlockApiLookup.get(Identifier.fromNamespaceAndPath(RFToolsBase.MODID, "machine_information"), IMachineInformation.class, Direction.class);
}
