package mcjty.rftoolsbase.modules.filter;

import mcjty.lib.McJtyLib;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.filter.client.GuiFilterModule;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class FilterModule implements IModule {

    public static final RegistryObject<FilterModuleItem> FILTER_MODULE = ITEMS.register("filter_module", FilterModuleItem::new);
    public static final RegistryObject<MenuType<FilterModuleContainer>> CONTAINER_FILTER_MODULE = CONTAINERS.register("filter_module", FilterModule::createFilterModuleContainer);

    private static MenuType<FilterModuleContainer> createFilterModuleContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            Player player = McJtyLib.proxy.getClientPlayer();
            FilterModuleContainer container = new FilterModuleContainer(windowId, player.blockPosition(), player);
            container.setupInventories(null, inv);
            return container;
        });
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            GuiFilterModule.register();
        });
    }

    @Override
    public void initConfig() {

    }
}
