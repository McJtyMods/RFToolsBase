package mcjty.rftoolsbase.modules.crafting;

import mcjty.lib.McJtyLib;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class CraftingSetup {

    public static void register() {
        // Needed to force class loading
    }

    public static final RegistryObject<Item> CRAFTING_CARD = ITEMS.register("crafting_card", CraftingCardItem::new);
    public static final RegistryObject<ContainerType<CraftingCardContainer>> CONTAINER_CRAFTING_CARD = CONTAINERS.register("crafting_card", CraftingSetup::createCraftingContainer);

    private static ContainerType<CraftingCardContainer> createCraftingContainer() {
        return IForgeContainerType.create((windowId, inv, data) -> {
            PlayerEntity player = McJtyLib.proxy.getClientPlayer();
            CraftingCardContainer container = new CraftingCardContainer(windowId, player.getPosition(), player);
            container.setupInventories(null, inv);
            return container;
        });
    }

}
