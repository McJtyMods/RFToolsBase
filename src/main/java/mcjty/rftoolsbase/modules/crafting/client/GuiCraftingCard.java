package mcjty.rftoolsbase.modules.crafting.client;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.events.BlockRenderEvent;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import mcjty.rftoolsbase.modules.crafting.network.PacketItemNBTToServer;
import mcjty.rftoolsbase.modules.crafting.network.PacketUpdateNBTItemCard;
import mcjty.rftoolsbase.network.RFToolsBaseMessages;
import mcjty.rftoolsbase.setup.CommandHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer.*;


public class GuiCraftingCard extends GenericGuiContainer<GenericTileEntity, CraftingCardContainer> {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 198;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/craftingcard.png");

    private BlockRender[] slots = new BlockRender[1 + INPUT_SLOTS];

    public GuiCraftingCard(CraftingCardContainer container, PlayerInventory inventory) {
        super(RFToolsBase.instance, null, container, inventory, /* @todo 1.14 */0, "craftingcard");
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void init() {
        super.init();

        Panel toplevel = new Panel(minecraft, this).setLayout(new PositionalLayout()).setBackground(iconLocation);
        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        toplevel.addChild(new Label(minecraft, this).setText("Regular 3x3 crafting recipe").setHorizontalAlignment(HorizontalAlignment.ALIGN_LEFT).setLayoutHint(new PositionalLayout.PositionalHint(10, 4, 160, 14)));
        toplevel.addChild(new Label(minecraft, this).setText("or more complicated recipes").setHorizontalAlignment(HorizontalAlignment.ALIGN_LEFT).setLayoutHint(new PositionalLayout.PositionalHint(10, 17, 160, 14)));
        toplevel.addChild(new Button(minecraft, this)
                .setText("Update")
                .setTooltips("Update the item in the output", "slot to the recipe in the", "3x3 grid")
                .addButtonEvent(parent -> RFToolsBaseMessages.INSTANCE.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, CommandHandler.CMD_TESTRECIPE, TypedMap.EMPTY)))
                .setLayoutHint(new PositionalLayout.PositionalHint(110, 57, 60, 14)));
        ToggleButton toggle = new ToggleButton(minecraft, this)
                .setCheckMarker(true)
                .setText("NBT")
                .setTooltips("Enable this if you want", "opcodes like 'get_ingredients'", "to strictly match on NBT")
                .setLayoutHint(new PositionalLayout.PositionalHint(110, 74, 60, 14));
        ItemStack heldItem = minecraft.player.getHeldItem(Hand.MAIN_HAND);
        if (!heldItem.isEmpty()) {
            toggle.setPressed(CraftingCardItem.isStrictNBT(heldItem));
        }
        toggle.addButtonEvent(parent -> {
            RFToolsBaseMessages.INSTANCE.sendToServer(new PacketUpdateNBTItemCard(
                    TypedMap.builder()
                            .put(new Key<>("strictnbt", Type.BOOLEAN), toggle.isPressed())
                            .build()));
        });

        toplevel.addChild(toggle);

        for (int y = 0 ; y < GRID_HEIGHT ; y++) {
            for (int x = 0 ; x < GRID_WIDTH ; x++) {
                int idx = y * GRID_WIDTH + x;
                createDummySlot(toplevel, idx, new PositionalLayout.PositionalHint(x * 18 + 10, y * 18 + 37, 18, 18), createSelectionEvent(idx));
            }
        }
        createDummySlot(toplevel, INPUT_SLOTS, new PositionalLayout.PositionalHint(10 + 8 * 18, 37, 18, 18), createSelectionEvent(INPUT_SLOTS));

        updateSlots();
        window = new Window(this, toplevel);
    }

    private void createDummySlot(Panel toplevel, int idx, PositionalLayout.PositionalHint hint, BlockRenderEvent selectionEvent) {
        slots[idx] = new BlockRender(minecraft, this) {

            @Override
            public List<String> getTooltips() {
                Object s = slots[idx].getRenderItem();
                if (s instanceof ItemStack) {
                    ItemStack stack = (ItemStack) s;
                    if (!stack.isEmpty()) {
                        ITooltipFlag flag = this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL;
                        List<ITextComponent> list = stack.getTooltip(this.mc.player, flag);

                        // @todo 1.14
                        for (int i = 0; i < list.size(); ++i) {
//                            if (i == 0) {
//                                list.set(i, stack.getRarity().rarityColor + list.get(i));
//                            } else {
//                                list.set(i, TextFormatting.GRAY + list.get(i));
//                            }
                        }

                        return list.stream().map(str -> str.getFormattedText()).collect(Collectors.toList());
                    } else {
                        return Collections.emptyList();
                    }
                } else {
                    return Collections.emptyList();
                }
            }
        }
                .setHilightOnHover(true)
                .setLayoutHint(hint);
        slots[idx].addSelectionEvent(selectionEvent);
        toplevel.addChild(slots[idx]);
    }

    private void updateSlots() {
        ItemStackList stacks = getStacks();
        if (stacks.isEmpty()) {
            return;
        }
        for (int i = 0 ; i < stacks.size() ; i++) {
            slots[i].setRenderItem(stacks.get(i));
        }
    }

    @Nonnull
    private ItemStackList getStacks() {
        ItemStack cardItem = minecraft.player.getHeldItem(Hand.MAIN_HAND);
        ItemStackList stacks = ItemStackList.EMPTY;
        if (!cardItem.isEmpty() && cardItem.getItem() instanceof CraftingCardItem) {
            stacks = CraftingCardItem.getStacksFromItem(cardItem);
        }
        return stacks;
    }

    private BlockRenderEvent createSelectionEvent(final int idx) {
        return new BlockRenderEvent() {
            @Override
            public void select(Widget parent) {
                ItemStack itemstack = minecraft.player.inventory.getItemStack();
                slots[idx].setRenderItem(itemstack);
                ItemStackList stacks = getStacks();
                if (!stacks.isEmpty()) {
                    stacks.set(idx, itemstack);
                    ItemStack cardItem = minecraft.player.getHeldItem(Hand.MAIN_HAND);
                    CraftingCardItem.putStacksInItem(cardItem, stacks);
                    RFToolsBaseMessages.INSTANCE.sendToServer(new PacketItemNBTToServer(cardItem.getTag()));
                }
            }

            @Override
            public void doubleClick(Widget parent) {

            }
        };
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        updateSlots();
        drawWindow();
    }
}
