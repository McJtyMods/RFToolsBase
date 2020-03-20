package mcjty.rftoolsbase.setup;


import mcjty.lib.gui.GenericGuiContainer;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.client.RenderWorldLastEventHandler;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.informationscreen.client.InformationScreenRenderer;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import mcjty.rftoolsbase.modules.various.VariousSetup;
import mcjty.rftoolsbase.modules.various.client.GuiFilterModule;
import mcjty.rftoolsbase.modules.various.items.FilterModuleContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent e) {
        InformationScreenRenderer.register();
        GenericGuiContainer.register(MachineInfuserSetup.CONTAINER_MACHINE_INFUSER.get(), GuiMachineInfuser::new);
        ScreenManager.registerFactory(CraftingSetup.CONTAINER_CRAFTING_CARD.get(), ClientRegistration::createCraftingCardGui);
        ScreenManager.registerFactory(VariousSetup.CONTAINER_FILTER_MODULE.get(), ClientRegistration::createFilterModuleGui);
        MinecraftForge.EVENT_BUS.addListener(RenderWorldLastEventHandler::tick);
    }

    private static GuiCraftingCard createCraftingCardGui(CraftingCardContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiCraftingCard(container, inventory);
    }

    private static GuiFilterModule createFilterModuleGui(FilterModuleContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiFilterModule(container, inventory);
    }
}
