package mcjty.rftoolsbase.worldgen;

import mcjty.lib.varia.DimensionId;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class DimensionCompositeFeature<F extends IFeatureConfig> extends ConfiguredFeature<F, Feature<F>> {

    private final DimensionId dimension;

    public DimensionCompositeFeature(ConfiguredFeature<F, Feature<F>> decoratedFeature, @Nonnull DimensionId dimension) {
        super(decoratedFeature.feature, decoratedFeature.config);
        this.dimension = dimension;
    }


    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random random, BlockPos pos) {
        if (DimensionId.fromWorld(reader.getWorld()).equals(dimension)) {
            return super.generate(reader, generator, random, pos);
        }
        return false;
    }
}
