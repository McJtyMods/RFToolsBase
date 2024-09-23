package mcjty.rftoolsbase.modules.crafting.client;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.events.BlockRenderEvent;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.BlockRender;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.Widgets;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import mcjty.rftoolsbase.setup.CommandHandler;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static mcjty.lib.gui.widgets.Widgets.button;
import static mcjty.lib.gui.widgets.Widgets.label;
import static mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer.*;


public class GuiCraftingCard extends GenericGuiContainer<GenericTileEntity, CraftingCardContainer> {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 198;

    private static final ResourceLocation iconLocation = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "textures/gui/craftingcard.png");

    private final BlockRender[] slots = new BlockRender[1 + INPUT_SLOTS];

    public GuiCraftingCard(CraftingCardContainer container, Inventory inventory, Component title) {
        super(container, inventory, title, CraftingCardItem.MANUAL);
        imageWidth = WIDTH;
        imageHeight = HEIGHT;
    }

    public static void register(RegisterMenuScreensEvent event) {
        event.register(CraftingModule.CONTAINER_CRAFTING_CARD.get(), GuiCraftingCard::new);
    }

    @Override
    public void init() {
        super.init();

        Panel toplevel = Widgets.positional().background(iconLocation);
        toplevel.bounds(leftPos, topPos, imageWidth, imageHeight);

        toplevel.children(label("Regular 3x3 crafting recipe").horizontalAlignment(HorizontalAlignment.ALIGN_LEFT).hint(10, 4, 160, 14));
        toplevel.children(label("or more complicated recipes").horizontalAlignment(HorizontalAlignment.ALIGN_LEFT).hint(10, 17, 160, 14));
        toplevel.children(button(110, 57, 60, 14, "Update")
                .tooltips("Update the item in the output", "slot to the recipe in the", "3x3 grid")
                .event(() -> RFToolsBaseMessages.sendToServer(PacketSendServerCommand.create(RFToolsBase.MODID, CommandHandler.CMD_TESTRECIPE, TypedMap.EMPTY))));
        // In 1.15 this no longer makes sense
//        ToggleButton toggle = new ToggleButton(minecraft, this)
//                .setCheckMarker(true)
//                .setText("NBT")
//                .setTooltips("Enable this if you want", "opcodes like 'get_ingredients'", "to strictly match on NBT")
//                .setLayoutHint(110, 74, 60, 14);
//        ItemStack heldItem = minecraft.player.getHeldItem(Hand.MAIN_HAND);
//        if (!heldItem.isEmpty()) {
//            toggle.setPressed(CraftingCardItem.isStrictNBT(heldItem));
//        }
//        toggle.addButtonEvent(parent -> {
//            RFToolsBaseMessages.INSTANCE.sendToServer(new PacketUpdateNBTItemCard(
//                    TypedMap.builder()
//                            .put(new Key<>("strictnbt", Type.BOOLEAN), toggle.isPressed())
//                            .build()));
//        });
//        toplevel.addChild(toggle);

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
        slots[idx] = new BlockRender() {

            @Override
            public List<String> getTooltips() {
                Object s = slots[idx].getRenderItem();
                if (s instanceof ItemStack stack) {
                    if (!stack.isEmpty()) {
                        TooltipFlag flag = this.mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
                        List<Component> list = stack.getTooltipLines(Item.TooltipContext.of(this.mc.level), this.mc.player, flag);

                        // @todo 1.14
                        for (int i = 0; i < list.size(); ++i) {
//                            if (i == 0) {
//                                list.set(i, stack.getRarity().rarityColor + list.get(i));
//                            } else {
//                                list.set(i, TextFormatting.GRAY + list.get(i));
//                            }
                        }

                        // @todo 1.16 used to be getFormattedText
                        return list.stream().map(Component::getString).collect(Collectors.toList());
                    } else {
                        return Collections.emptyList();
                    }
                } else {
                    return Collections.emptyList();
                }
            }
        }
                .hilightOnHover(true)
                .hint(hint);
        slots[idx].event(selectionEvent);
        toplevel.children(slots[idx]);
    }

    private void updateSlots() {
        ItemStackList stacks = getStacks();
        if (stacks.isEmpty()) {
            return;
        }
        for (int i = 0 ; i < stacks.size() ; i++) {
            slots[i].renderItem(stacks.get(i));
        }
    }

    @Nonnull
    private ItemStackList getStacks() {
        ItemStack cardItem = minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStackList stacks = ItemStackList.EMPTY;
        if (!cardItem.isEmpty() && cardItem.getItem() instanceof CraftingCardItem) {
            stacks = CraftingCardItem.getStacksFromItem(cardItem);
        }
        return stacks;
    }

    private BlockRenderEvent createSelectionEvent(final int idx) {
        return new BlockRenderEvent() {
            @Override
            public void select() {
                ItemStack itemstack = menu.getCarried();
//                ItemStack itemstack = minecraft.player.getInventory().getSelected();    // @todo 1.18 is this right? Was getCarried()
                slots[idx].renderItem(itemstack);
                ItemStackList stacks = getStacks();
                if (!stacks.isEmpty()) {
                    stacks.set(idx, itemstack);
                    ItemStack cardItem = minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);
                    CraftingCardItem.putStacksInItem(cardItem, stacks);
                    // @todo 1.21
//                    RFToolsBaseMessages.sendToServer(PacketItemNBTToServer.create(cardItem.getTag()));
                }
            }

            @Override
            public void doubleClick() {

            }
        };
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int x, int y) {
        updateSlots();
        drawWindow(graphics, partialTicks, x, y);
    }
}
