package mcjty.rftoolsbase.modules.tablet;

import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.tablet.client.GuiTablet;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class TabletModule implements IModule {

    public static final RegistryObject<TabletItem> TABLET = ITEMS.register("tablet", TabletItem::new);
    public static final RegistryObject<MenuType<TabletContainer>> CONTAINER_TABLET = CONTAINERS.register("tablet", TabletModule::createTabletContainer);

    public static final RegistryObject<TabletItem> TABLET_FILLED = ITEMS.register("tablet_filled", TabletItem::new);

    private static MenuType<TabletContainer> createTabletContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            Player player = inv.player;
            TabletContainer container = new TabletContainer(windowId, player.blockPosition(), player);
            container.setupInventories(new TabletItemHandler(player), inv);
            return container;
        });
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
        GuiTablet.register();
    }

    @Override
    public void initConfig() {

    }
}
