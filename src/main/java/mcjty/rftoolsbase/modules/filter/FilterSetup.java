package mcjty.rftoolsbase.modules.filter;

import mcjty.lib.McJtyLib;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class FilterSetup {

    public static void register() {
        // Needed to force class loading
    }

    public static final RegistryObject<FilterModuleItem> FILTER_MODULE = ITEMS.register("filter_module", FilterModuleItem::new);
    public static final RegistryObject<ContainerType<FilterModuleContainer>> CONTAINER_FILTER_MODULE = CONTAINERS.register("filter_module", FilterSetup::createFilterModuleContainer);

    private static ContainerType<FilterModuleContainer> createFilterModuleContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            PlayerEntity player = McJtyLib.proxy.getClientPlayer();
            FilterModuleContainer container = new FilterModuleContainer(windowId, player.getPosition(), player);
            container.setupInventories(null, inv);
            return container;
        });
    }
}
