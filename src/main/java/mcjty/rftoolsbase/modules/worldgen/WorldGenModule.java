package mcjty.rftoolsbase.modules.worldgen;

import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.worldgen.blocks.DimensionalShardBlock;
import mcjty.rftoolsbase.modules.worldgen.config.WorldGenConfig;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.rftoolsbase.setup.Registration.BLOCKS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class WorldGenModule implements IModule {

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_OVERWORLD = BLOCKS.register("dimensionalshard_overworld", DimensionalShardBlock::new);
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_OVERWORLD_ITEM = ITEMS.register("dimensionalshard_overworld", () -> new BlockItem(DIMENSIONAL_SHARD_OVERWORLD.get(), Registration.createStandardProperties()));

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_NETHER = BLOCKS.register("dimensionalshard_nether", DimensionalShardBlock::new);
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_NETHER_ITEM = ITEMS.register("dimensionalshard_nether", () -> new BlockItem(DIMENSIONAL_SHARD_NETHER.get(), Registration.createStandardProperties()));

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_END = BLOCKS.register("dimensionalshard_end", DimensionalShardBlock::new);
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_END_ITEM = ITEMS.register("dimensionalshard_end", () -> new BlockItem(DIMENSIONAL_SHARD_END.get(), Registration.createStandardProperties()));

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {

    }

    @Override
    public void initConfig() {
        WorldGenConfig.setupWorldgenConfig();
    }
}
