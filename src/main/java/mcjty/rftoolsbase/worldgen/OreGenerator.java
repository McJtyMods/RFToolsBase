package mcjty.rftoolsbase.worldgen;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.modules.worldgen.config.WorldGenConfig;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.function.Predicate;

public class OreGenerator {

    private static final Predicate<BlockState> IS_NETHERACK = state -> state.getBlock() == Blocks.NETHERRACK;
    private static final Predicate<BlockState> IS_ENDSTONE = state -> state.getBlock() == Blocks.END_STONE;

    public static ConfiguredFeature OVERWORLD_SHARDS;
    public static ConfiguredFeature NETHER_SHARDS;
    public static ConfiguredFeature END_SHARDS;

    public static final RuleTest ENDSTONE_TEST = new TagMatchTest(Tags.Blocks.END_STONES);

    public static void registerConfiguredFeatures() {
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;

        ConfiguredFeature<OreConfiguration, ?> overworldFeature = Feature.ORE
                .configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get().defaultBlockState(),
                        WorldGenConfig.OVERWORLD_ORE_VEINSIZE.get()));
        OVERWORLD_SHARDS = new DimensionCompositeFeature(overworldFeature, Level.OVERWORLD)
                .decorated(Registration.COUNT_PLACEMENT.get().configured(new CountPlacementConfig(
                        WorldGenConfig.OVERWORLD_ORE_MINY.get(),
                        0,
                        WorldGenConfig.OVERWORLD_ORE_MAXY.get() - WorldGenConfig.OVERWORLD_ORE_MINY.get(),
                        WorldGenConfig.OVERWORLD_ORE_CHANCES.get())));
        Registry.register(registry, new ResourceLocation(RFToolsBase.MODID, "dimshard_overworld"), OVERWORLD_SHARDS);

        ConfiguredFeature<OreConfiguration, ?> netherFeature = Feature.ORE
                .configured(new OreConfiguration(OreConfiguration.Predicates.NETHER_ORE_REPLACEABLES, WorldGenModule.DIMENSIONAL_SHARD_NETHER.get().defaultBlockState(),
                        WorldGenConfig.NETHER_ORE_VEINSIZE.get()));
        NETHER_SHARDS = new DimensionCompositeFeature(netherFeature, Level.NETHER)
                .decorated(Registration.COUNT_PLACEMENT.get().configured(new CountPlacementConfig(
                        WorldGenConfig.NETHER_ORE_MINY.get(),
                        0,
                        WorldGenConfig.NETHER_ORE_MAXY.get() - WorldGenConfig.NETHER_ORE_MINY.get(),
                        WorldGenConfig.NETHER_ORE_CHANCES.get())));
        Registry.register(registry, new ResourceLocation(RFToolsBase.MODID, "dimshard_nether"), NETHER_SHARDS);

        ConfiguredFeature<OreConfiguration, ?> endFeature = Feature.ORE
                .configured(new OreConfiguration(ENDSTONE_TEST, WorldGenModule.DIMENSIONAL_SHARD_END.get().defaultBlockState(),
                        WorldGenConfig.END_ORE_VEINSIZE.get()));
        END_SHARDS = new DimensionCompositeFeature(endFeature, Level.END)
                .decorated(Registration.COUNT_PLACEMENT.get().configured(new CountPlacementConfig(
                        WorldGenConfig.END_ORE_MINY.get(),
                        0,
                        WorldGenConfig.END_ORE_MAXY.get() - WorldGenConfig.END_ORE_MINY.get(),
                        WorldGenConfig.END_ORE_CHANCES.get())));
        Registry.register(registry, new ResourceLocation(RFToolsBase.MODID, "dimshard_end"), END_SHARDS);
    }

    public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.BiomeCategory.NETHER) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NETHER_SHARDS);
        } else if (event.getCategory() == Biome.BiomeCategory.THEEND) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, END_SHARDS);
        } else {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OVERWORLD_SHARDS);
        }
    }
}
