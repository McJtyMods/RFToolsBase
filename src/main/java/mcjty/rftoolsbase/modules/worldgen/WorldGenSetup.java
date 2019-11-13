package mcjty.rftoolsbase.modules.worldgen;

import mcjty.lib.blocks.BaseBlockItem;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.worldgen.blocks.DimensionalShardBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

public class WorldGenSetup {

    @ObjectHolder("rftoolsbase:dimensionalshard_overworld")
    public static DimensionalShardBlock DIMENSIONAL_SHARD_OVERWORLD;

    @ObjectHolder("rftoolsbase:dimensionalshard_nether")
    public static DimensionalShardBlock DIMENSIONAL_SHARD_NETHER;

    @ObjectHolder("rftoolsbase:dimensionalshard_end")
    public static DimensionalShardBlock DIMENSIONAL_SHARD_END;

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new DimensionalShardBlock(DimensionalShardBlock.OreType.ORE_OVERWORLD));
        event.getRegistry().register(new DimensionalShardBlock(DimensionalShardBlock.OreType.ORE_NETHER));
        event.getRegistry().register(new DimensionalShardBlock(DimensionalShardBlock.OreType.ORE_END));
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        Item.Properties properties = new Item.Properties().group(RFToolsBase.setup.getTab());
        event.getRegistry().register(new BaseBlockItem(DIMENSIONAL_SHARD_OVERWORLD, properties));
        event.getRegistry().register(new BaseBlockItem(DIMENSIONAL_SHARD_NETHER, properties));
        event.getRegistry().register(new BaseBlockItem(DIMENSIONAL_SHARD_END, properties));
    }
}
