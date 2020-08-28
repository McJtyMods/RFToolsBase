package mcjty.rftoolsbase.modules.crafting;

import mcjty.lib.McJtyLib;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class CraftingModule implements IModule {

    public static final RegistryObject<Item> CRAFTING_CARD = ITEMS.register("crafting_card", CraftingCardItem::new);
    public static final RegistryObject<ContainerType<CraftingCardContainer>> CONTAINER_CRAFTING_CARD = CONTAINERS.register("crafting_card", CraftingModule::createCraftingContainer);

    private static ContainerType<CraftingCardContainer> createCraftingContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            PlayerEntity player = McJtyLib.proxy.getClientPlayer();
            CraftingCardContainer container = new CraftingCardContainer(windowId, player.getPosition(), player);
            container.setupInventories(null, inv);
            return container;
        });
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CONTAINER_CRAFTING_CARD.get(), CraftingModule::createCraftingCardGui);
    }

    private static GuiCraftingCard createCraftingCardGui(CraftingCardContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiCraftingCard(container, inventory);
    }

    @Override
    public void initConfig() {

    }
}
