package mcjty.rftoolsbase.modules.worldgen;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.worldgen.blocks.DimensionalShardBlock;
import mcjty.rftoolsbase.setup.Registration;
import mcjty.rftoolsbase.worldgen.OreGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import mcjty.lib.setup.DeferredBlock;
import mcjty.lib.setup.DeferredItem;

import java.util.List;

import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.BLOCKS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class WorldGenModule implements IModule {

    static { OreGenerator.init(); }

    public static final DeferredBlock<Block> DIMENSIONAL_SHARD_OVERWORLD = BLOCKS.register("dimensionalshard_overworld", DimensionalShardBlock::new);
    public static final DeferredItem<Item> DIMENSIONAL_SHARD_OVERWORLD_ITEM = ITEMS.register("dimensionalshard_overworld", tab(properties -> new BlockItem(DIMENSIONAL_SHARD_OVERWORLD.get(), properties.useBlockDescriptionPrefix())));

    public static final DeferredBlock<Block> DIMENSIONAL_SHARD_NETHER = BLOCKS.register("dimensionalshard_nether", DimensionalShardBlock::new);
    public static final DeferredItem<Item> DIMENSIONAL_SHARD_NETHER_ITEM = ITEMS.register("dimensionalshard_nether", tab(properties -> new BlockItem(DIMENSIONAL_SHARD_NETHER.get(), properties.useBlockDescriptionPrefix())));

    public static final DeferredBlock<Block> DIMENSIONAL_SHARD_END = BLOCKS.register("dimensionalshard_end", DimensionalShardBlock::new);
    public static final DeferredItem<Item> DIMENSIONAL_SHARD_END_ITEM = ITEMS.register("dimensionalshard_end", tab(properties -> new BlockItem(DIMENSIONAL_SHARD_END.get(), properties.useBlockDescriptionPrefix())));

    public static final TagKey<Block> DIMENSIONAL_SHARD_ORE = TagTools.createBlockTagKey(Identifier.fromNamespaceAndPath("c", "ores/dimensional_shard"));
    public static final TagKey<Item> DIMENSIONAL_SHARD_ORE_ITEM = TagTools.createItemTagKey(Identifier.fromNamespaceAndPath("c", "ores/dimensional_shard"));


    @Override
    public void init() {
        addOre(net.minecraft.tags.BiomeTags.IS_OVERWORLD, "dimshard_overworld");
        addOre(net.minecraft.tags.BiomeTags.IS_OVERWORLD, "dimshard_dimensions");
        addOre(net.minecraft.tags.BiomeTags.IS_NETHER, "dimshard_nether");
        addOre(net.minecraft.tags.BiomeTags.IS_END, "dimshard_end");

    }

    private static void addOre(net.minecraft.tags.TagKey<net.minecraft.world.level.biome.Biome> biomes, String feature) {
        net.fabricmc.fabric.api.biome.v1.BiomeModifications.addFeature(
                net.fabricmc.fabric.api.biome.v1.BiomeSelectors.tag(biomes),
                net.minecraft.world.level.levelgen.GenerationStep.Decoration.UNDERGROUND_ORES,
                net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.PLACED_FEATURE,
                        Identifier.fromNamespaceAndPath("rftoolsbase", feature)));
    }

    @Override
    public void initClient() {

    }

    @Override
    public void initConfig() {
    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.builder(DIMENSIONAL_SHARD_OVERWORLD, DIMENSIONAL_SHARD_OVERWORLD_ITEM)
                        .ironPickaxeTags()
                        .silkTouchLoot(provider, VariousModule.DIMENSIONALSHARD, 4f, 5f)
                        .blockTags(List.of(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK, net.minecraft.resources.Identifier.parse("c:ores")), DIMENSIONAL_SHARD_ORE))
                        .itemTags(List.of(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.parse("c:ores")), DIMENSIONAL_SHARD_ORE_ITEM)),
                Dob.builder(DIMENSIONAL_SHARD_NETHER, DIMENSIONAL_SHARD_NETHER_ITEM)
                        .ironPickaxeTags()
                        .silkTouchLoot(provider, VariousModule.DIMENSIONALSHARD, 4f, 5f)
                        .blockTags(List.of(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK, net.minecraft.resources.Identifier.parse("c:ores")), DIMENSIONAL_SHARD_ORE))
                        .itemTags(List.of(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.parse("c:ores")), DIMENSIONAL_SHARD_ORE_ITEM)),
                Dob.builder(DIMENSIONAL_SHARD_END, DIMENSIONAL_SHARD_END_ITEM)
                        .ironPickaxeTags()
                        .silkTouchLoot(provider, VariousModule.DIMENSIONALSHARD, 4f, 5f)
                        .blockTags(List.of(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK, net.minecraft.resources.Identifier.parse("c:ores")), DIMENSIONAL_SHARD_ORE))
                        .itemTags(List.of(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.parse("c:ores")), DIMENSIONAL_SHARD_ORE_ITEM))
        );
    }
}
