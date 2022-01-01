package mcjty.rftoolsbase.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class DimensionCompositeFeature<F extends FeatureConfiguration> extends ConfiguredFeature<F, Feature<F>> {

    private final ResourceKey<Level> dimension;

    public DimensionCompositeFeature(ConfiguredFeature<F, Feature<F>> decoratedFeature, @Nonnull ResourceKey<Level> dimension) {
        super(decoratedFeature.feature, decoratedFeature.config);
        this.dimension = dimension;
    }


    @Override
    public boolean place(WorldGenLevel reader, @Nonnull ChunkGenerator generator, @Nonnull Random random, @Nonnull BlockPos pos) {
        if (Objects.equals(dimension, reader.getLevel().dimension())) {
            return super.place(reader, generator, random, pos);
        }
        return false;
    }
}
