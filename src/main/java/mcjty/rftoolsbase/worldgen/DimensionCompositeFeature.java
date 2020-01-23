package mcjty.rftoolsbase.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class DimensionCompositeFeature<F extends IFeatureConfig> extends ConfiguredFeature<F, Feature<F>> {

    private final DimensionType dimension;

    public DimensionCompositeFeature(ConfiguredFeature<F, Feature<F>> decoratedFeature, @Nonnull DimensionType dimension) {
        super(decoratedFeature.feature, decoratedFeature.config);
        this.dimension = dimension;
    }

    @Override
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos) {
        if (world.getDimension().getType().equals(dimension)) {
            return super.place(world, generator, rand, pos);
        }
        return false;
    }
}
