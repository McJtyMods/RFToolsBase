package mcjty.rftoolsbase.worldgen;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.function.Predicate;

public class DimensionOreFeatureConfig implements IFeatureConfig {
    private final Predicate<BlockState> filter;
    private final DimensionType dimension;
    private final int size;
    private final BlockState state;

    public DimensionOreFeatureConfig(Predicate<BlockState> filter, DimensionType dimension, BlockState state, int size) {
        this.size = size;
        this.state = state;
        this.filter = filter;
        this.dimension = dimension;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return null;    // @todo check?
    }

}