package mcjty.rftoolsbase.setup;


import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenSetup;
import mcjty.rftoolsbase.modules.worldgen.WorldGenSetup;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
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
        WorldGenSetup.registerBlocks(event);
        MachineInfuserSetup.registerBlocks(event);
        InformationScreenSetup.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new SmartWrenchItem(SmartWrenchMode.MODE_WRENCH));
        event.getRegistry().register(new SmartWrenchItem(SmartWrenchMode.MODE_SELECT));
        event.getRegistry().register(new Item(new Item.Properties()
                .maxStackSize(64)
                .group(RFToolsBase.setup.getTab()))
                .setRegistryName("dimensionalshard"));

        event.getRegistry().register(new Item(new Item.Properties().group(RFToolsBase.setup.getTab()).maxStackSize(16)).setRegistryName(RFToolsBase.MODID, "infused_diamond"));
        event.getRegistry().register(new Item(new Item.Properties().group(RFToolsBase.setup.getTab()).maxStackSize(16)).setRegistryName(RFToolsBase.MODID, "infused_enderpearl"));

        Item.Properties properties = new Item.Properties().group(RFToolsBase.setup.getTab());
        event.getRegistry().register(new Item(properties).setRegistryName("machine_frame"));
        event.getRegistry().register(new Item(properties).setRegistryName("machine_base"));

        WorldGenSetup.registerItems(event);
        MachineInfuserSetup.registerItems(event);
        CraftingSetup.registerItems(event);
        InformationScreenSetup.registerItems(event);
    }

    @SubscribeEvent
    public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> event) {
        MachineInfuserSetup.registerTiles(event);
        InformationScreenSetup.registerTiles(event);
    }

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        MachineInfuserSetup.registerContainers(event);
        CraftingSetup.registerContainers(event);
        InformationScreenSetup.registerContainers(event);
    }

}
