package mcjty.rftoolsbase.setup;


import mcjty.lib.gui.GenericGuiContainer;
import mcjty.rftoolsbase.client.RenderWorldLastEventHandler;
import mcjty.rftoolsbase.keys.KeyBindings;
import mcjty.rftoolsbase.keys.KeyInputHandler;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.filter.FilterSetup;
import mcjty.rftoolsbase.modules.filter.client.GuiFilterModule;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.informationscreen.client.InformationScreenRenderer;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import mcjty.rftoolsbase.modules.tablet.TabletSetup;
import mcjty.rftoolsbase.modules.tablet.client.GuiTablet;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent e) {
        DeferredWorkQueue.runLater(() -> {
            GenericGuiContainer.register(MachineInfuserSetup.CONTAINER_MACHINE_INFUSER.get(), GuiMachineInfuser::new);
            ScreenManager.registerFactory(CraftingSetup.CONTAINER_CRAFTING_CARD.get(), ClientSetup::createCraftingCardGui);
            ScreenManager.registerFactory(FilterSetup.CONTAINER_FILTER_MODULE.get(), ClientSetup::createFilterModuleGui);
            ScreenManager.registerFactory(TabletSetup.CONTAINER_TABLET.get(), ClientSetup::createTabletGui);
        });
        InformationScreenRenderer.register();
        MinecraftForge.EVENT_BUS.addListener(RenderWorldLastEventHandler::tick);
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        KeyBindings.init();
    }

    private static GuiCraftingCard createCraftingCardGui(CraftingCardContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiCraftingCard(container, inventory);
    }

    private static GuiFilterModule createFilterModuleGui(FilterModuleContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiFilterModule(container, inventory);
    }

    private static GuiTablet createTabletGui(TabletContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiTablet(container, inventory);
    }
}
