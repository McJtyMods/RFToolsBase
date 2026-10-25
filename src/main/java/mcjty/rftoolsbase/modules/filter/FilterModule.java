package mcjty.rftoolsbase.modules.filter;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.crafting.data.CraftingCardData;
import mcjty.rftoolsbase.modules.filter.client.GuiFilterModule;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import mcjty.lib.setup.DeferredHolder;
import mcjty.lib.setup.DeferredItem;

import java.util.function.Supplier;

import static mcjty.lib.datagen.DataGen.has;
import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.*;

public class FilterModule implements IModule {

    public static final DeferredItem<FilterModuleItem> FILTER_MODULE = ITEMS.register("filter_module", tab(FilterModuleItem::new));
    public static final Supplier<MenuType<FilterModuleContainer>> CONTAINER_FILTER_MODULE = CONTAINERS.register("filter_module", FilterModule::createFilterModuleContainer);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FilterModuleData>> ITEM_FILTERMODULE_DATA = COMPONENTS.registerComponentType(
            "filtermodule_data",
            builder -> builder
                    .persistent(FilterModuleData.CODEC)
                    .networkSynchronized(FilterModuleData.STREAM_CODEC));


    private static MenuType<FilterModuleContainer> createFilterModuleContainer() {
        return new ExtendedMenuType<>((windowId, inv, data) -> {
            Player player = SafeClientTools.getClientPlayer();
            FilterModuleContainer container = new FilterModuleContainer(windowId, player.blockPosition(), player);
            container.setupInventories(null, inv);
            return container;
        }, mcjty.lib.container.MenuTools.CODEC);
    }


    @Override
    public void init() {

    }

    @Override
    public void initClient() {
        registerMenuScreens();
    }

    public void registerMenuScreens() {
        GuiFilterModule.register();
    }

    @Override
    public void initConfig() {

    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.itemBuilder(FILTER_MODULE)
                        .shaped(builder -> builder
                                .define('h', Items.HOPPER)
                                .unlockedBy("hopper", has(Items.HOPPER)),
                                " h ", "rir", " p ")
        );
    }
}
