package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.modules.worldgen.config.WorldGenConfig;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.function.Predicate;

public class OreGenerator {

    private static final Predicate<BlockState> IS_NETHERACK = state -> state.getBlock() == Blocks.NETHERRACK;
    private static final Predicate<BlockState> IS_ENDSTONE = state -> state.getBlock() == Blocks.END_STONE;

    public static ConfiguredFeature OVERWORLD_SHARDS;
    public static ConfiguredFeature NETHER_SHARDS;
    public static ConfiguredFeature END_SHARDS;

    public static final RuleTest ENDSTONE_TEST = new TagMatchRuleTest(Tags.Blocks.END_STONES);

    public static void registerConfiguredFeatures() {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;

        ConfiguredFeature<OreFeatureConfig, ?> overworldFeature = Feature.ORE
                .configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get().defaultBlockState(),
                        WorldGenConfig.OVERWORLD_ORE_VEINSIZE.get()));
        OVERWORLD_SHARDS = new DimensionCompositeFeature(overworldFeature, World.OVERWORLD)
                .decorated(Registration.COUNT_PLACEMENT.get().configured(new CountPlacementConfig(
                        WorldGenConfig.OVERWORLD_ORE_MINY.get(),
                        0,
                        WorldGenConfig.OVERWORLD_ORE_MAXY.get() - WorldGenConfig.OVERWORLD_ORE_MINY.get(),
                        WorldGenConfig.OVERWORLD_ORE_CHANCES.get())));
        Registry.register(registry, new ResourceLocation(RFToolsBase.MODID, "dimshard_overworld"), OVERWORLD_SHARDS);

        ConfiguredFeature<OreFeatureConfig, ?> netherFeature = Feature.ORE
                .configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHER_ORE_REPLACEABLES, WorldGenModule.DIMENSIONAL_SHARD_NETHER.get().defaultBlockState(),
                        WorldGenConfig.NETHER_ORE_VEINSIZE.get()));
        NETHER_SHARDS = new DimensionCompositeFeature(netherFeature, World.NETHER)
                .decorated(Registration.COUNT_PLACEMENT.get().configured(new CountPlacementConfig(
                        WorldGenConfig.NETHER_ORE_MINY.get(),
                        0,
                        WorldGenConfig.NETHER_ORE_MAXY.get() - WorldGenConfig.NETHER_ORE_MINY.get(),
                        WorldGenConfig.NETHER_ORE_CHANCES.get())));
        Registry.register(registry, new ResourceLocation(RFToolsBase.MODID, "dimshard_nether"), NETHER_SHARDS);

        ConfiguredFeature<OreFeatureConfig, ?> endFeature = Feature.ORE
                .configured(new OreFeatureConfig(ENDSTONE_TEST, WorldGenModule.DIMENSIONAL_SHARD_END.get().defaultBlockState(),
                        WorldGenConfig.END_ORE_VEINSIZE.get()));
        END_SHARDS = new DimensionCompositeFeature(endFeature, World.END)
                .decorated(Registration.COUNT_PLACEMENT.get().configured(new CountPlacementConfig(
                        WorldGenConfig.END_ORE_MINY.get(),
                        0,
                        WorldGenConfig.END_ORE_MAXY.get() - WorldGenConfig.END_ORE_MINY.get(),
                        WorldGenConfig.END_ORE_CHANCES.get())));
        Registry.register(registry, new ResourceLocation(RFToolsBase.MODID, "dimshard_end"), END_SHARDS);
    }

    public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.Category.NETHER) {
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, NETHER_SHARDS);
        } else if (event.getCategory() == Biome.Category.THEEND) {
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, END_SHARDS);
        } else {
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, OVERWORLD_SHARDS);
        }
    }
}
