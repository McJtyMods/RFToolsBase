package mcjty.rftoolsbase.worldgen;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class DimensionCompositeFeature<F extends IFeatureConfig> extends ConfiguredFeature<F, Feature<F>> {

    private final RegistryKey<World> dimension;

    public DimensionCompositeFeature(ConfiguredFeature<F, Feature<F>> decoratedFeature, @Nonnull RegistryKey<World> dimension) {
        super(decoratedFeature.feature, decoratedFeature.config);
        this.dimension = dimension;
    }


    @Override
    public boolean place(ISeedReader reader, ChunkGenerator generator, Random random, BlockPos pos) {
        if (Objects.equals(dimension, reader.getLevel().dimension())) {
            return super.place(reader, generator, random, pos);
        }
        return false;
    }
}
