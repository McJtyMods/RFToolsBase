package mcjty.rftoolsbase.modules.crafting;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.data.CraftingCardData;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import mcjty.lib.setup.DeferredHolder;
import mcjty.lib.setup.DeferredItem;

import java.util.function.Supplier;

import static mcjty.lib.datagen.DataGen.has;
import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.*;

public class CraftingModule implements IModule {

    public static final DeferredItem<Item> CRAFTING_CARD = ITEMS.register("crafting_card", tab(CraftingCardItem::new));
    public static final Supplier<MenuType<CraftingCardContainer>> CONTAINER_CRAFTING_CARD = CONTAINERS.register("crafting_card", CraftingModule::createCraftingContainer);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CraftingCardData>> ITEM_CRAFTINGCARD_DATA = COMPONENTS.registerComponentType(
            "craftingcard_data",
            builder -> builder
                    .persistent(CraftingCardData.CODEC)
                    .networkSynchronized(CraftingCardData.STREAM_CODEC));

    private static MenuType<CraftingCardContainer> createCraftingContainer() {
        return new ExtendedMenuType<>((windowId, inv, data) -> {
            Player player = SafeClientTools.getClientPlayer();
            CraftingCardContainer container = new CraftingCardContainer(windowId, player.blockPosition(), player);
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
        GuiCraftingCard.register();
    }

    @Override
    public void initConfig() {

    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
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
