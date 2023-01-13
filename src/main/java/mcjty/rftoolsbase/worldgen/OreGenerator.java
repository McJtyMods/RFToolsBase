package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class OreGenerator {

    public static void init() {
    }

    public static final RuleTest IN_ENDSTONE = new TagMatchTest(Tags.Blocks.END_STONES);


    public static final RegistryObject<ConfiguredFeature<?, ?>> OVERWORLD_SHARDS = Registration.CONFIGURED_FEATURES.register(
            "dimshard_overworld",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get().defaultBlockState(),
                    5)));
    public static final RegistryObject<PlacedFeature> PLACEMENT_OVERWORLD_SHARDS = Registration.PLACED_FEATURES.register(
            "dimshard_overworld",
            () -> new PlacedFeature(OVERWORLD_SHARDS.getHolder().get(), List.of(
                    CountPlacement.of(2),
                    InSquarePlacement.spread(),
                    new DimensionBiomeFilter(id -> id.equals(Level.OVERWORLD)),
                    HeightRangePlacement.uniform(VerticalAnchor.absolute(-15), VerticalAnchor.absolute(40))
            )));

    public static final RegistryObject<ConfiguredFeature<?, ?>> DIMENSION_SHARDS = Registration.CONFIGURED_FEATURES.register(
            "dimshard_dimensions",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get().defaultBlockState(),
                    10)));
    public static final RegistryObject<PlacedFeature> PLACEMENT_DIMENSION_SHARDS = Registration.PLACED_FEATURES.register(
            "dimshard_dimensions",
            () -> new PlacedFeature(DIMENSION_SHARDS.getHolder().get(), List.of(
                    CountPlacement.of(6),
                    InSquarePlacement.spread(),
                    new DimensionBiomeFilter(id -> id.equals(Level.OVERWORLD)),
                    HeightRangePlacement.uniform(VerticalAnchor.absolute(-15), VerticalAnchor.absolute(40))
            )));

    public static final RegistryObject<ConfiguredFeature<?, ?>> NETHER_SHARDS = Registration.CONFIGURED_FEATURES.register(
            "dimshard_nether",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), WorldGenModule.DIMENSIONAL_SHARD_NETHER.get().defaultBlockState(),
                    8)));
    public static final RegistryObject<PlacedFeature> PLACEMENT_NETHER_SHARDS = Registration.PLACED_FEATURES.register(
            "dimshard_nether",
            () -> new PlacedFeature(NETHER_SHARDS.getHolder().get(), List.of(
                    CountPlacement.of(8),
                    InSquarePlacement.spread(),
                    BiomeFilter.biome(),
                    HeightRangePlacement.uniform(VerticalAnchor.absolute(2), VerticalAnchor.absolute(40))
            )));

    public static final RegistryObject<ConfiguredFeature<?, ?>> END_SHARDS = Registration.CONFIGURED_FEATURES.register(
            "dimshard_end",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(IN_ENDSTONE, WorldGenModule.DIMENSIONAL_SHARD_END.get().defaultBlockState(),
                    10)));
    public static final RegistryObject<PlacedFeature> PLACEMENT_END_SHARDS = Registration.PLACED_FEATURES.register(
            "dimshard_end",
            () -> new PlacedFeature(END_SHARDS.getHolder().get(), List.of(
                    CountPlacement.of(8),
                    InSquarePlacement.spread(),
                    BiomeFilter.biome(),
                    HeightRangePlacement.uniform(VerticalAnchor.absolute(2), VerticalAnchor.absolute(80))
            )));
}
