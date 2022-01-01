package mcjty.rftoolsbase.modules.crafting;

import mcjty.lib.modules.IModule;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.CONTAINERS;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class CraftingModule implements IModule {

    public static final RegistryObject<Item> CRAFTING_CARD = ITEMS.register("crafting_card", CraftingCardItem::new);
    public static final RegistryObject<MenuType<CraftingCardContainer>> CONTAINER_CRAFTING_CARD = CONTAINERS.register("crafting_card", CraftingModule::createCraftingContainer);

    private static MenuType<CraftingCardContainer> createCraftingContainer() {
        return IForgeMenuType.create((windowId, inv, data) -> {
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
    public void initConfig() {

    }
}
