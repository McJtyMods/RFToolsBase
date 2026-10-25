package mcjty.rftoolsbase.modules.tablet;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.tablet.client.GuiTablet;
import mcjty.rftoolsbase.modules.tablet.data.TabletData;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItemHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import mcjty.lib.setup.DeferredHolder;
import mcjty.lib.setup.DeferredItem;

import java.util.function.Supplier;

import static mcjty.lib.datagen.DataGen.has;
import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.*;

public class TabletModule implements IModule {

    public static final DeferredItem<TabletItem> TABLET = ITEMS.register("tablet", tab(TabletItem::new));
    public static final Supplier<MenuType<TabletContainer>> CONTAINER_TABLET = CONTAINERS.register("tablet", TabletModule::createTabletContainer);

    public static final DeferredItem<TabletItem> TABLET_FILLED = ITEMS.register("tablet_filled", TabletItem::new);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TabletData>> ITEM_TABLET_DATA = COMPONENTS.registerComponentType(
            "tablet_data",
            builder -> builder
                    .persistent(TabletData.CODEC)
                    .networkSynchronized(TabletData.STREAM_CODEC));


    private static MenuType<TabletContainer> createTabletContainer() {
        return new ExtendedMenuType<>((windowId, inv, data) -> {
            Player player = inv.player;
            TabletContainer container = new TabletContainer(windowId, player.blockPosition(), player);
            container.setupInventories(new TabletItemHandler(player), inv);
            return container;
        }, mcjty.lib.container.MenuTools.CODEC);
    }


    @Override
    public void init() {

    }

    @Override
    public void initClient() {
        registerScreens();
    }

    public void registerScreens() {
        GuiTablet.register();
    }

    @Override
    public void initConfig() {
    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.itemBuilder(TABLET)
                        .shaped(builder -> builder
                                        .define('g', net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.parse("c:nuggets/gold")))
                                        .define('Q', Items.QUARTZ_BLOCK)
                                        .unlockedBy("quartz", has(Blocks.QUARTZ_BLOCK)),
                                "geg", "RQR", "gRg")
        );
    }
}
