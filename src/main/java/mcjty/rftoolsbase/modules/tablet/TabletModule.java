package mcjty.rftoolsbase.modules.tablet;

import mcjty.lib.McJtyLib;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.tablet.client.GuiTablet;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItemHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class TabletModule implements IModule {

    public static final RegistryObject<TabletItem> TABLET = ITEMS.register("tablet", TabletItem::new);
    public static final RegistryObject<ContainerType<TabletContainer>> CONTAINER_TABLET = CONTAINERS.register("tablet", TabletModule::createTabletContainer);

    public static final RegistryObject<TabletItem> TABLET_FILLED = ITEMS.register("tablet_filled", TabletItem::new);

    private static ContainerType<TabletContainer> createTabletContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            PlayerEntity player = McJtyLib.proxy.getClientPlayer();
            TabletContainer container = new TabletContainer(windowId, player.getPosition(), player);
            container.setupInventories(new TabletItemHandler(player), inv);
            return container;
        });
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CONTAINER_TABLET.get(), TabletModule::createTabletGui);
    }

    private static GuiTablet createTabletGui(TabletContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiTablet(container, inventory);
    }

    @Override
    public void initConfig() {

    }
}
