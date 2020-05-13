package mcjty.rftoolsbase.modules.worldgen;

import mcjty.rftoolsbase.modules.worldgen.blocks.DimensionalShardBlock;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.*;

public class WorldGenSetup {

    public static void register() {
        // Needed to force class loading
    }

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_OVERWORLD = BLOCKS.register("dimensionalshard_overworld", DimensionalShardBlock::new);
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_OVERWORLD_ITEM = ITEMS.register("dimensionalshard_overworld", () -> new BlockItem(DIMENSIONAL_SHARD_OVERWORLD.get(), Registration.createStandardProperties()));

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_NETHER = BLOCKS.register("dimensionalshard_nether", DimensionalShardBlock::new);
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_NETHER_ITEM = ITEMS.register("dimensionalshard_nether", () -> new BlockItem(DIMENSIONAL_SHARD_NETHER.get(), Registration.createStandardProperties()));

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_END = BLOCKS.register("dimensionalshard_end", DimensionalShardBlock::new);
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_END_ITEM = ITEMS.register("dimensionalshard_end", () -> new BlockItem(DIMENSIONAL_SHARD_END.get(), Registration.createStandardProperties()));
}
