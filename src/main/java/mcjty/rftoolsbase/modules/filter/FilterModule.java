package mcjty.rftoolsbase.modules.filter;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.filter.client.GuiFilterModule;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.Supplier;

import static mcjty.lib.datagen.DataGen.has;
import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class FilterModule implements IModule {

    public static final DeferredItem<FilterModuleItem> FILTER_MODULE = ITEMS.register("filter_module", tab(FilterModuleItem::new));
    public static final Supplier<MenuType<FilterModuleContainer>> CONTAINER_FILTER_MODULE = CONTAINERS.register("filter_module", FilterModule::createFilterModuleContainer);

    private static MenuType<FilterModuleContainer> createFilterModuleContainer() {
        return IMenuTypeExtension.create((windowId, inv, data) -> {
            Player player = SafeClientTools.getClientPlayer();
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
    public void initConfig(IEventBus bus) {

    }

    @Override
    public void initDatagen(DataGen dataGen) {
        dataGen.add(
                Dob.itemBuilder(FILTER_MODULE)
                        .shaped(builder -> builder
                                .define('h', Items.HOPPER)
                                .unlockedBy("hopper", has(Items.HOPPER)),
                                " h ", "rir", " p ")
        );
    }
}
