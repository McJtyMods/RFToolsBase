package mcjty.rftoolsbase.modules.tablet;

import mcjty.lib.McJtyLib;
import mcjty.rftoolsbase.modules.tablet.items.TabletItemHandler;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mcjty.rftoolsbase.RFToolsBase.MODID;

public class TabletSetup {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
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
