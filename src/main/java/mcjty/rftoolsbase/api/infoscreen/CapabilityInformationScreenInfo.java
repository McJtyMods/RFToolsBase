package mcjty.rftoolsbase.api.infoscreen;

import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;

public class CapabilityInformationScreenInfo {

    public static final BlockApiLookup<IInformationScreenInfo, @Nullable Direction> INFORMATION_SCREEN_INFO_CAPABILITY = BlockApiLookup.get(Identifier.fromNamespaceAndPath(RFToolsBase.MODID, "information_screen"), IInformationScreenInfo.class, Direction.class);
}
