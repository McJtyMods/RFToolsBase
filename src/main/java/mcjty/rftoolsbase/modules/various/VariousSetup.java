package mcjty.rftoolsbase.modules.various;

import mcjty.lib.McJtyLib;
import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.various.items.FilterModuleInventory;
import mcjty.rftoolsbase.modules.various.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.various.items.SmartWrenchItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mcjty.rftoolsbase.RFToolsBase.MODID;

public class VariousSetup {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SmartWrenchItem> SMARTWRENCH = ITEMS.register("smartwrench", () -> new SmartWrenchItem(SmartWrenchMode.MODE_WRENCH));
    public static final RegistryObject<SmartWrenchItem> SMARTWRENCH_SELECT = ITEMS.register("smartwrench_select", () -> new SmartWrenchItem(SmartWrenchMode.MODE_SELECT));

    public static final RegistryObject<FilterModuleItem> FILTER_MODULE = ITEMS.register("filter_module", FilterModuleItem::new);
    public static final RegistryObject<ContainerType<FilterModuleContainer>> CONTAINER_FILTER_MODULE = CONTAINERS.register("filter_module", VariousSetup::createFilterModuleContainer);

    public static final RegistryObject<Item> DIMENSIONALSHARD = ITEMS.register("dimensionalshard", VariousSetup::createDimensionalShard);
    public static final RegistryObject<Item> INFUSED_DIAMOND = ITEMS.register("infused_diamond", VariousSetup::createItem16);
    public static final RegistryObject<Item> INFUSED_ENDERPEARL = ITEMS.register("infused_enderpearl", VariousSetup::createItem16);

    public static final RegistryObject<Item> MACHINE_FRAME = ITEMS.register("machine_frame", () -> new Item(RFToolsBase.createStandardProperties()));
    public static final RegistryObject<Item> MACHINE_BASE = ITEMS.register("machine_base", () -> new Item(RFToolsBase.createStandardProperties()));

    private static Item createItem16() {
        return new Item(new Item.Properties().group(RFToolsBase.setup.getTab()).maxStackSize(16));
    }

    private static Item createDimensionalShard() {
        return new Item(new Item.Properties()
                .maxStackSize(64)
                .group(RFToolsBase.setup.getTab()));
    }

    private static ContainerType<FilterModuleContainer> createFilterModuleContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            PlayerEntity player = McJtyLib.proxy.getClientPlayer();
            FilterModuleContainer container = new FilterModuleContainer(windowId, player.getPosition(), player);
            container.setupInventories(new FilterModuleInventory(player), inv);
            return container;
        });
    }
}
