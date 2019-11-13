package mcjty.rftoolsbase.setup;


import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.blocks.BaseBlockItem;
import mcjty.lib.container.GenericContainer;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.blocks.DimensionalShardBlock;
import mcjty.rftoolsbase.blocks.ModBlocks;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import mcjty.rftoolsbase.items.SmartWrenchItem;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new DimensionalShardBlock(DimensionalShardBlock.OreType.ORE_OVERWORLD));
        event.getRegistry().register(new DimensionalShardBlock(DimensionalShardBlock.OreType.ORE_NETHER));
        event.getRegistry().register(new DimensionalShardBlock(DimensionalShardBlock.OreType.ORE_END));

        MachineInfuserSetup.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new SmartWrenchItem(SmartWrenchMode.MODE_WRENCH));
        event.getRegistry().register(new SmartWrenchItem(SmartWrenchMode.MODE_SELECT));
        event.getRegistry().register(new Item(new Item.Properties()
                .maxStackSize(64)
                .group(RFToolsBase.setup.getTab()))
                .setRegistryName("dimensionalshard"));

        Item.Properties properties = new Item.Properties().group(RFToolsBase.setup.getTab());
        event.getRegistry().register(new BaseBlockItem(ModBlocks.DIMENSIONAL_SHARD_OVERWORLD, properties));
        event.getRegistry().register(new BaseBlockItem(ModBlocks.DIMENSIONAL_SHARD_NETHER, properties));
        event.getRegistry().register(new BaseBlockItem(ModBlocks.DIMENSIONAL_SHARD_END, properties));

        event.getRegistry().register(new Item(new Item.Properties().group(RFToolsBase.setup.getTab()).maxStackSize(16)).setRegistryName(RFToolsBase.MODID, "infused_diamond"));
        event.getRegistry().register(new Item(new Item.Properties().group(RFToolsBase.setup.getTab()).maxStackSize(16)).setRegistryName(RFToolsBase.MODID, "infused_enderpearl"));

        event.getRegistry().register(new Item(properties).setRegistryName("machine_frame"));
        event.getRegistry().register(new Item(properties).setRegistryName("machine_base"));

        MachineInfuserSetup.registerItems(event);
        CraftingSetup.registerItems(event);
    }

    @SubscribeEvent
    public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> event) {
        MachineInfuserSetup.registerTiles(event);
    }

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        MachineInfuserSetup.registerContainers(event);
        CraftingSetup.registerContainers(event);
    }

}
