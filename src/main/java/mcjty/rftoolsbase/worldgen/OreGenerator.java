package mcjty.rftoolsbase.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.function.Predicate;

public class OreGenerator {

    private static final Predicate<BlockState> IS_NETHERACK = state -> state.getBlock() == Blocks.NETHERRACK;
    private static final Predicate<BlockState> IS_ENDSTONE = state -> state.getBlock() == Blocks.END_STONE;

    public static void init() {
        // @todo 1.16
//        for (Biome biome : ForgeRegistries.BIOMES) {
//
//            int overworldChances = Config.OVERWORLD_ORE_CHANCES.get();
//            if (overworldChances > 0) {
//                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
//                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WorldGenSetup.DIMENSIONAL_SHARD_OVERWORLD.get().getDefaultState(),
//                                Config.OVERWORLD_ORE_VEINSIZE.get()))
//                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(
//                                overworldChances,
//                                Config.OVERWORLD_ORE_MINY.get(),
//                                0,
//                                Config.OVERWORLD_ORE_MAXY.get() - Config.OVERWORLD_ORE_MINY.get())))
//                );
//            }
//
//
//            int netherChances = Config.NETHER_ORE_CHANCES.get();
//            if (netherChances > 0) {
//                ConfiguredFeature<OreFeatureConfig, ?> netherFeature = Feature.ORE
//                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WorldGenSetup.DIMENSIONAL_SHARD_NETHER.get().getDefaultState(),
//                                Config.NETHER_ORE_VEINSIZE.get()));
//                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new DimensionCompositeFeature(netherFeature, DimensionType.THE_NETHER)
//                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(
//                                overworldChances,
//                                Config.NETHER_ORE_MINY.get(),
//                                0,
//                                Config.NETHER_ORE_MAXY.get() - Config.NETHER_ORE_MINY.get())))
//                );
//            }
////            ConfiguredFeature<?> featureEnd = Biome.createDecoratedFeature(Feature.ORE,
////                    new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.DIMENSIONAL_SHARD_END.getDefaultState(), 8),
////                    Placement.COUNT_RANGE, placementConfig);
////            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new DimensionCompositeFeature(featureEnd, DimensionType.THE_END));
//        }
    }
}
