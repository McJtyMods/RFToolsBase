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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.BLOCKS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class WorldGenModule implements IModule {

    public static final DeferredBlock<Block> DIMENSIONAL_SHARD_OVERWORLD = BLOCKS.register("dimensionalshard_overworld", DimensionalShardBlock::new);
    public static final DeferredItem<Item> DIMENSIONAL_SHARD_OVERWORLD_ITEM = ITEMS.register("dimensionalshard_overworld", tab(() -> new BlockItem(DIMENSIONAL_SHARD_OVERWORLD.get(), Registration.createStandardProperties())));

    public static final DeferredBlock<Block> DIMENSIONAL_SHARD_NETHER = BLOCKS.register("dimensionalshard_nether", DimensionalShardBlock::new);
    public static final DeferredItem<Item> DIMENSIONAL_SHARD_NETHER_ITEM = ITEMS.register("dimensionalshard_nether", tab(() -> new BlockItem(DIMENSIONAL_SHARD_NETHER.get(), Registration.createStandardProperties())));

    public static final DeferredBlock<Block> DIMENSIONAL_SHARD_END = BLOCKS.register("dimensionalshard_end", DimensionalShardBlock::new);
    public static final DeferredItem<Item> DIMENSIONAL_SHARD_END_ITEM = ITEMS.register("dimensionalshard_end", tab(() -> new BlockItem(DIMENSIONAL_SHARD_END.get(), Registration.createStandardProperties())));

    public static final TagKey<Block> DIMENSIONAL_SHARD_ORE = TagTools.createBlockTagKey(ResourceLocation.fromNamespaceAndPath("c", "ores/dimensional_shard"));
    public static final TagKey<Item> DIMENSIONAL_SHARD_ORE_ITEM = TagTools.createItemTagKey(ResourceLocation.fromNamespaceAndPath("c", "ores/dimensional_shard"));

    public WorldGenModule() {
        OreGenerator.init();
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {

    }

    @Override
    public void initConfig(IEventBus bus) {
    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.builder(DIMENSIONAL_SHARD_OVERWORLD, DIMENSIONAL_SHARD_OVERWORLD_ITEM)
                        .ironPickaxeTags()
                        .silkTouchLoot(provider, VariousModule.DIMENSIONALSHARD, 4f, 5f)
                        .blockTags(List.of(Tags.Blocks.ORES, DIMENSIONAL_SHARD_ORE))
                        .itemTags(List.of(Tags.Items.ORES, DIMENSIONAL_SHARD_ORE_ITEM)),
                Dob.builder(DIMENSIONAL_SHARD_NETHER, DIMENSIONAL_SHARD_NETHER_ITEM)
                        .ironPickaxeTags()
                        .silkTouchLoot(provider, VariousModule.DIMENSIONALSHARD, 4f, 5f)
                        .blockTags(List.of(Tags.Blocks.ORES, DIMENSIONAL_SHARD_ORE))
                        .itemTags(List.of(Tags.Items.ORES, DIMENSIONAL_SHARD_ORE_ITEM)),
                Dob.builder(DIMENSIONAL_SHARD_END, DIMENSIONAL_SHARD_END_ITEM)
                        .ironPickaxeTags()
                        .silkTouchLoot(provider, VariousModule.DIMENSIONALSHARD, 4f, 5f)
                        .blockTags(List.of(Tags.Blocks.ORES, DIMENSIONAL_SHARD_ORE))
                        .itemTags(List.of(Tags.Items.ORES, DIMENSIONAL_SHARD_ORE_ITEM))
        );
    }
}
