package mcjty.rftoolsbase.modules.crafting;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.lib.setup.DeferredItem;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.Supplier;

import static mcjty.lib.datagen.DataGen.has;
import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class CraftingModule implements IModule {

    public static final DeferredItem<Item> CRAFTING_CARD = ITEMS.register("crafting_card", tab(CraftingCardItem::new));
    public static final Supplier<MenuType<CraftingCardContainer>> CONTAINER_CRAFTING_CARD = CONTAINERS.register("crafting_card", CraftingModule::createCraftingContainer);

    private static MenuType<CraftingCardContainer> createCraftingContainer() {
        return IMenuTypeExtension.create((windowId, inv, data) -> {
            Player player = SafeClientTools.getClientPlayer();
            CraftingCardContainer container = new CraftingCardContainer(windowId, player.blockPosition(), player);
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
            GuiCraftingCard.register();
        });
    }

    @Override
    public void initConfig(IEventBus bus) {

    }

    @Override
    public void initDatagen(DataGen dataGen) {
        dataGen.add(
                Dob.itemBuilder(CRAFTING_CARD)
                        .shaped(builder -> builder
                                .define('C', Items.CRAFTING_TABLE)
                                .unlockedBy("crafter", has(Items.CRAFTING_TABLE)),
                                8,
                                "pC", "rp")
        );
    }
}
