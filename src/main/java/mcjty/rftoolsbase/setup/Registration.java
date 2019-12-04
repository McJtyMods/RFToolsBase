package mcjty.rftoolsbase.setup;


import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.items.SmartWrenchItem;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {

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

        CraftingSetup.registerItems(event);
    }

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        CraftingSetup.registerContainers(event);
    }

}
