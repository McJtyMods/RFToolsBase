package mcjty.rftoolsbase.modules.tablet.client;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.ToggleButton;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import javax.annotation.Nonnull;

import static mcjty.lib.gui.widgets.Widgets.positional;
import static mcjty.rftoolsbase.modules.tablet.items.TabletContainer.NUM_SLOTS;


public class GuiTablet extends GenericGuiContainer<GenericTileEntity, TabletContainer> {
    public static final int TABLET_WIDTH = 180;
    public static final int TABLET_HEIGHT = 188;

    private static final ResourceLocation iconLocation = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "textures/gui/tablet.png");

    private ToggleButton[] buttons;

    public GuiTablet(TabletContainer container, Inventory inventory, Component title) {
        super(container, inventory, title, TabletItem.MANUAL);
        imageWidth = TABLET_WIDTH;
        imageHeight = TABLET_HEIGHT;
    }

    public static void register(RegisterMenuScreensEvent event) {
        event.register(TabletModule.CONTAINER_TABLET.get(), GuiTablet::new);
    }

    @Override
    public void init() {
        super.init();

        Panel toplevel = positional().background(iconLocation);

        buttons = new ToggleButton[NUM_SLOTS];
        for (int i = 0 ; i < NUM_SLOTS ; i++) {
            int finalI = i;
            buttons[i] = new ToggleButton().hint(14 + i * (18+5), 12 + 20, 19, 8).event(() -> setActive(finalI));
            toplevel.children(buttons[i]);
        }

        toplevel.bounds(leftPos, topPos, imageWidth, imageHeight);
        window = new Window(this, toplevel);
    }

    private void updateActiveButton(int current) {
        for (int i = 0 ; i < NUM_SLOTS ; i++) {
            buttons[i].pressed(i == current);
        }
    }

    private InteractionHand getHand() {
        if (minecraft.player == null || minecraft.player.getUsedItemHand() == null) {
            return InteractionHand.MAIN_HAND;
        } else {
            return minecraft.player.getUsedItemHand();
        }
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics graphics, int p_230451_2_, int p_230451_3_) {
        super.renderLabels(graphics, p_230451_2_, p_230451_3_);
        ItemStack heldItem = minecraft.player.getItemInHand(getHand());
        updateActiveButton(TabletItem.getCurrentSlot(heldItem));
    }

    private void setActive(int i) {
        ItemStack heldItem = minecraft.player.getItemInHand(getHand());
        TabletItem.setCurrentSlot(minecraft.player, heldItem, i);
        updateActiveButton(i);
        syncStack();
    }

    private void syncStack() {
        RFToolsBaseMessages.sendToServer(PacketSyncHandItem.create(minecraft.player));
    }
}
