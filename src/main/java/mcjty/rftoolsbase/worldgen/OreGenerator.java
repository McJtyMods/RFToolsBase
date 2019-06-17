package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;

public class OreGenerator {

    private static final Predicate<BlockState> IS_NETHERACK = state -> state.getBlock() == Blocks.NETHERRACK;
    private static final Predicate<BlockState> IS_ENDSTONE = state -> state.getBlock() == Blocks.END_STONE;

    public static void init() {
        for (Biome biome : ForgeRegistries.BIOMES) {

            CountRangeConfig placementConfig = new CountRangeConfig(5, 2, 0, 40);

            ConfiguredFeature<?> featureOverworld = Biome.createDecoratedFeature(Feature.ORE,
                    new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.DIMENSIONAL_SHARD_OVERWORLD.getDefaultState(), 8),
                    Placement.COUNT_RANGE, placementConfig);
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new DimensionCompositeFeature(featureOverworld, DimensionType.OVERWORLD));

            ConfiguredFeature<?> featureNether = Biome.createDecoratedFeature(Feature.ORE,
                    new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.DIMENSIONAL_SHARD_NETHER.getDefaultState(), 8),
                    Placement.COUNT_RANGE, placementConfig);
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new DimensionCompositeFeature(featureNether, DimensionType.NETHER));

//            ConfiguredFeature<?> featureEnd = Biome.createDecoratedFeature(Feature.ORE,
//                    new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.DIMENSIONAL_SHARD_END.getDefaultState(), 8),
//                    Placement.COUNT_RANGE, placementConfig);
//            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new DimensionCompositeFeature(featureEnd, DimensionType.THE_END));
        }
    }
}
