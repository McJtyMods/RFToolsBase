package mcjty.rftoolsbase.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.*;

/**
 * A biome filter that also checks if the dimension is right
 */
public class DimensionBiomeFilter extends PlacementFilter {

    private final boolean isOverworld;

    private static final DimensionBiomeFilter INSTANCE_OVERWORLD = new DimensionBiomeFilter(true);
    private static final DimensionBiomeFilter INSTANCE_DIMENSION = new DimensionBiomeFilter(false);
    public static MapCodec<PlacementModifier> CODEC_OVERWORLD = MapCodec.unit(() -> INSTANCE_OVERWORLD);
    public static MapCodec<PlacementModifier> CODEC_DIMENSION = MapCodec.unit(() -> INSTANCE_DIMENSION);

    public DimensionBiomeFilter(boolean isOverworld) {
        this.isOverworld = isOverworld;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        boolean overworld = context.getLevel().getLevel().dimension() == Level.OVERWORLD;
        if (overworld == isOverworld) {
            PlacedFeature placedfeature = context.topFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature"));
            Holder<Biome> biome = context.getLevel().getBiome(pos);
            return biome.value().getGenerationSettings().hasFeature(placedfeature);
        } else {
            return false;
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.BIOME_FILTER;
    }
}
