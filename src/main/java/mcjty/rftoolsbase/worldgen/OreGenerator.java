package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.modules.worldgen.config.WorldGenConfig;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;

public class OreGenerator {

    public static final RuleTest IN_ENDSTONE = new TagMatchTest(Tags.Blocks.END_STONES);

    public static Holder<PlacedFeature> DIMENSION_SHARDS;
    public static Holder<PlacedFeature> OVERWORLD_SHARDS;
    public static Holder<PlacedFeature> NETHER_SHARDS;
    public static Holder<PlacedFeature> END_SHARDS;

    public static void registerConfiguredFeatures() {
        OreConfiguration dimensionConfig = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get().defaultBlockState(),
                WorldGenConfig.DIMENSION_ORE_VEINSIZE.get());
        DIMENSION_SHARDS = registerPlacedFeature("rftoolsbase:dimshard_dimensions", new ConfiguredFeature<>(Feature.ORE, dimensionConfig),
                CountPlacement.of(WorldGenConfig.DIMENSION_ORE_CHANCES.get()),
                InSquarePlacement.spread(),
                new DimensionBiomeFilter(id -> !id.equals(Level.OVERWORLD)),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(WorldGenConfig.DIMENSION_ORE_MINY.get()), VerticalAnchor.absolute(WorldGenConfig.DIMENSION_ORE_MAXY.get())));

        OreConfiguration overworldConfig = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get().defaultBlockState(),
                WorldGenConfig.OVERWORLD_ORE_VEINSIZE.get());
        OVERWORLD_SHARDS = registerPlacedFeature("rftoolsbase:dimshard_overworld", new ConfiguredFeature<>(Feature.ORE, overworldConfig),
                CountPlacement.of(WorldGenConfig.OVERWORLD_ORE_CHANCES.get()),
                InSquarePlacement.spread(),
                new DimensionBiomeFilter(id -> id.equals(Level.OVERWORLD)),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(WorldGenConfig.OVERWORLD_ORE_MINY.get()), VerticalAnchor.absolute(WorldGenConfig.OVERWORLD_ORE_MAXY.get())));

        OreConfiguration netherConfig = new OreConfiguration(OreFeatures.NETHER_ORE_REPLACEABLES, WorldGenModule.DIMENSIONAL_SHARD_NETHER.get().defaultBlockState(),
                WorldGenConfig.NETHER_ORE_VEINSIZE.get());
        NETHER_SHARDS = registerPlacedFeature("rftoolsbase:dimshard_nether", new ConfiguredFeature<>(Feature.ORE, netherConfig),
                CountPlacement.of(WorldGenConfig.NETHER_ORE_CHANCES.get()),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(WorldGenConfig.NETHER_ORE_MINY.get()), VerticalAnchor.absolute(WorldGenConfig.NETHER_ORE_MAXY.get())));

        OreConfiguration endConfig = new OreConfiguration(IN_ENDSTONE, WorldGenModule.DIMENSIONAL_SHARD_END.get().defaultBlockState(),
                WorldGenConfig.END_ORE_VEINSIZE.get());
        END_SHARDS = registerPlacedFeature("rftoolsbase:dimshard_end", new ConfiguredFeature<>(Feature.ORE, endConfig),
                CountPlacement.of(WorldGenConfig.END_ORE_CHANCES.get()),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(WorldGenConfig.END_ORE_MINY.get()), VerticalAnchor.absolute(WorldGenConfig.END_ORE_MAXY.get())));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> Holder<PlacedFeature> registerPlacedFeature(String registryName, ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        return PlacementUtils.register(registryName, Holder.direct(feature), placementModifiers);
    }
}
