package mcjty.rftoolsbase.modules.crafting;

import mcjty.lib.McJtyLib;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

public class CraftingSetup {

    @ObjectHolder("rftoolsbase:crafting_card")
    public static Item CRAFTING_CARD;

    @ObjectHolder("rftoolsbase:crafting_card")
    public static ContainerType<CraftingCardContainer> CONTAINER_CRAFTING_CARD;

    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new CraftingCardItem());
    }

    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            PlayerEntity player = McJtyLib.proxy.getClientPlayer();
            CraftingCardContainer container = new CraftingCardContainer(windowId, player.getPosition(), player);
            container.setupInventories(null, inv);
            return container;
        }).setRegistryName("crafting_card"));
    }

}
