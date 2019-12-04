package mcjty.rftoolsbase.setup;


import mcjty.lib.gui.GenericGuiContainer;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.informationscreen.client.InformationScreenRenderer;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent e) {
        InformationScreenRenderer.register();
        GenericGuiContainer.register(MachineInfuserSetup.CONTAINER_MACHINE_INFUSER.get(), GuiMachineInfuser::new);
        ScreenManager.registerFactory(CraftingSetup.CONTAINER_CRAFTING_CARD, ClientRegistration::create);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    }

    private static GuiCraftingCard create(CraftingCardContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiCraftingCard(container, inventory);
    }
}
