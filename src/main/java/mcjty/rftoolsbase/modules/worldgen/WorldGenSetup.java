package mcjty.rftoolsbase.modules.worldgen;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.worldgen.blocks.DimensionalShardBlock;
import mcjty.rftoolsbase.modules.worldgen.blocks.DimensionalShardBlock.OreType;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mcjty.rftoolsbase.RFToolsBase.MODID;

public class WorldGenSetup {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_OVERWORLD = BLOCKS.register("dimensionalshard_overworld", () -> new DimensionalShardBlock(OreType.ORE_OVERWORLD));
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_OVERWORLD_ITEM = ITEMS.register("dimensionalshard_overworld", () -> new BlockItem(DIMENSIONAL_SHARD_OVERWORLD.get(), RFToolsBase.createStandardProperties()));

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_NETHER = BLOCKS.register("dimensionalshard_nether", () -> new DimensionalShardBlock(OreType.ORE_NETHER));
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_NETHER_ITEM = ITEMS.register("dimensionalshard_nether", () -> new BlockItem(DIMENSIONAL_SHARD_NETHER.get(), RFToolsBase.createStandardProperties()));

    public static final RegistryObject<Block> DIMENSIONAL_SHARD_END = BLOCKS.register("dimensionalshard_end", () -> new DimensionalShardBlock(OreType.ORE_END));
    public static final RegistryObject<Item> DIMENSIONAL_SHARD_END_ITEM = ITEMS.register("dimensionalshard_end", () -> new BlockItem(DIMENSIONAL_SHARD_END.get(), RFToolsBase.createStandardProperties()));
}
