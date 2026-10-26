package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public class OreGenerator {

    public static void init() {
    }

    public static final Supplier<MapCodec<? extends PlacementModifier>> FILTER_OVERWORLD = Registration.PLACEMENT_MODIFIERS.register("filter_overworld",
            () -> DimensionBiomeFilter.CODEC_OVERWORLD);
    public static final Supplier<MapCodec<? extends PlacementModifier>> FILTER_DIMENSIONS = Registration.PLACEMENT_MODIFIERS.register("filter_dimensions",
            () -> DimensionBiomeFilter.CODEC_DIMENSION);
}
