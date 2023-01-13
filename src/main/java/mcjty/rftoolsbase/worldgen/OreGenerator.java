package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.RegistryObject;

public class OreGenerator {

    public static void init() {
    }

    public static final RegistryObject<PlacementModifierType<?>> FILTER_OVERWORLD = Registration.PLACEMENT_MODIFIERS.register("filter_overworld",
            () -> (PlacementModifierType<PlacementModifier>) () -> DimensionBiomeFilter.CODEC_OVERWORLD);
    public static final RegistryObject<PlacementModifierType<?>> FILTER_DIMENSIONS = Registration.PLACEMENT_MODIFIERS.register("filter_dimensions",
            () -> (PlacementModifierType<PlacementModifier>) () -> DimensionBiomeFilter.CODEC_DIMENSION);
}
