package mcjty.rftoolsbase.modules.tablet;

import mcjty.lib.McJtyLib;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class TabletSetup {

    public static void register() {
        // Needed to force class loading
    }

    public static final RegistryObject<TabletItem> TABLET = ITEMS.register("tablet", TabletItem::new);
    public static final RegistryObject<ContainerType<TabletContainer>> CONTAINER_TABLET = CONTAINERS.register("tablet", TabletSetup::createTabletContainer);

    public static final RegistryObject<TabletItem> TABLET_FILLED = ITEMS.register("tablet_filled", TabletItem::new);

    private static ContainerType<TabletContainer> createTabletContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            PlayerEntity player = McJtyLib.proxy.getClientPlayer();
            TabletContainer container = new TabletContainer(windowId, player.getPosition(), player);
            container.setupInventories(new TabletItemHandler(player), inv);
            return container;
        });
    }

}
