package mcjty.rftoolsbase.modules.tablet.client;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

import static mcjty.lib.gui.widgets.Widgets.positional;
import static mcjty.rftoolsbase.modules.tablet.items.TabletContainer.NUM_SLOTS;


public class GuiTablet extends GenericGuiContainer<GenericTileEntity, TabletContainer> {
    public static final int TABLET_WIDTH = 180;
    public static final int TABLET_HEIGHT = 188;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/tablet.png");

    private ToggleButton[] buttons;

    public GuiTablet(TabletContainer container, PlayerInventory inventory) {
        super(null, container, inventory, TabletItem.MANUAL);
        imageWidth = TABLET_WIDTH;
        imageHeight = TABLET_HEIGHT;
    }

    public static void register() {
        ScreenManager.register(TabletModule.CONTAINER_TABLET.get(), GuiTablet::createTabletGui);
    }

    @Nonnull
    private static GuiTablet createTabletGui(TabletContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        return new GuiTablet(container, inventory);
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

    private Hand getHand() {
        if (minecraft.player == null || minecraft.player.getUsedItemHand() == null) {
            return Hand.MAIN_HAND;
        } else {
            return minecraft.player.getUsedItemHand();
        }
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack stack, int p_230451_2_, int p_230451_3_) {
        super.renderLabels(stack, p_230451_2_, p_230451_3_);
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
        RFToolsBaseMessages.INSTANCE.sendToServer(new PacketSyncHandItem(minecraft.player));
    }
}
