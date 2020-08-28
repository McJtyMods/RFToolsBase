package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.modules.worldgen.config.WorldGenConfig;
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

            int overworldChances = WorldGenConfig.OVERWORLD_ORE_CHANCES.get();
            if (overworldChances > 0) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get().getDefaultState(),
                                WorldGenConfig.OVERWORLD_ORE_VEINSIZE.get()))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(
                                overworldChances,
                                WorldGenConfig.OVERWORLD_ORE_MINY.get(),
                                0,
                                WorldGenConfig.OVERWORLD_ORE_MAXY.get() - WorldGenConfig.OVERWORLD_ORE_MINY.get())))
                );
            }


            int netherChances = WorldGenConfig.NETHER_ORE_CHANCES.get();
            if (netherChances > 0) {
                ConfiguredFeature<OreFeatureConfig, ?> netherFeature = Feature.ORE
                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WorldGenModule.DIMENSIONAL_SHARD_NETHER.get().getDefaultState(),
                                WorldGenConfig.NETHER_ORE_VEINSIZE.get()));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new DimensionCompositeFeature(netherFeature, DimensionType.THE_NETHER)
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(
                                overworldChances,
                                WorldGenConfig.NETHER_ORE_MINY.get(),
                                0,
                                WorldGenConfig.NETHER_ORE_MAXY.get() - WorldGenConfig.NETHER_ORE_MINY.get())))
                );
            }
//            ConfiguredFeature<?> featureEnd = Biome.createDecoratedFeature(Feature.ORE,
//                    new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.DIMENSIONAL_SHARD_END.getDefaultState(), 8),
//                    Placement.COUNT_RANGE, placementConfig);
//            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new DimensionCompositeFeature(featureEnd, DimensionType.THE_END));
        }
    }
}
