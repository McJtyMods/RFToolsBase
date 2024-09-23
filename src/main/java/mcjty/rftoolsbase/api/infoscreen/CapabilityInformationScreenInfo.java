package mcjty.rftoolsbase.api.infoscreen;

import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class CapabilityInformationScreenInfo {

    public static final BlockCapability<IInformationScreenInfo, @Nullable Direction> INFORMATION_SCREEN_INFO_CAPABILITY = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "information_screen"), IInformationScreenInfo.class);
}
