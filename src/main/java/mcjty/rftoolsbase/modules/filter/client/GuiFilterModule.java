package mcjty.rftoolsbase.modules.filter.client;

import mcjty.lib.McJtyLib;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.TagSelectorWindow;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleInventory;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.filter.network.PacketUpdateNBTItemFilter;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.Set;

import static mcjty.lib.gui.widgets.Widgets.*;


public class GuiFilterModule extends GenericGuiContainer<GenericTileEntity, FilterModuleContainer> {
    public static final int CONTROLLER_WIDTH = 230;
    public static final int CONTROLLER_HEIGHT = 188;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/filtermodule.png");
    private static final ResourceLocation guiElements = new ResourceLocation(RFToolsBase.MODID, "textures/gui/guielements.png");

    private final TagSelectorWindow selector = new TagSelectorWindow();

    private ImageChoiceLabel blacklistMode;
    private ImageChoiceLabel damageMode;
    private ImageChoiceLabel nbtMode;
    private ImageChoiceLabel modMode;

    private Button remove;
    private Button expand;
    private Button addTags;

    private WidgetList list;
    private Slider slider;

    public GuiFilterModule(FilterModuleContainer container, PlayerInventory inventory) {
        super(RFToolsBase.instance, null, container, inventory, /* @todo 1.14 */0, "storfilter");
        xSize = CONTROLLER_WIDTH;
        ySize = CONTROLLER_HEIGHT;
    }

    @Override
    public void init() {
        super.init();

        remove = button(5, 106, 50, 15, "Remove").tooltips("Remove current selection").event(this::removeSelection);
        expand = button(5, 121, 50, 15, "Expand").tooltips("Expand item to tags").event(this::expandToTags);
        addTags = button(5, 137, 50, 15, "Add tags").tooltips("Add tags").event(this::addTagWindow);

        blacklistMode = imageChoice(5, 152, 16, 16).tooltips("Black or whitelist mode").event((newChoice) -> updateSettings());
        blacklistMode.choice("Black", "Blacklist items", guiElements, 14 * 16, 32);
        blacklistMode.choice("White", "Whitelist items", guiElements, 15 * 16, 32);

        damageMode = imageChoice(21, 152, 16, 16).tooltips("Filter ignoring damage").event((newChoice) -> updateSettings());
        damageMode.choice("Off", "Ignore damage", guiElements, 6 * 16, 32);
        damageMode.choice("On", "Damage must match", guiElements, 7 * 16, 32);

        nbtMode = imageChoice(5, 168, 16, 16).tooltips("Filter ignoring NBT").event((newChoice) -> updateSettings());
        nbtMode.choice("Off", "Ignore NBT", guiElements, 8 * 16, 32);
        nbtMode.choice("On", "NBT must match", guiElements, 9 * 16, 32);

        modMode = imageChoice(21, 168, 16, 16).tooltips("Filter ignoring mod").event((newChoice) -> updateSettings());
        modMode.choice("Off", "Don't match on mod", guiElements, 12 * 16, 32);
        modMode.choice("On", "Only mod must match", guiElements, 13 * 16, 32);

        list = list(5, 4, 207, 99).name("list");
        slider = slider(212, 4, 10, 99).scrollableName("list");

        CompoundNBT tagCompound = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getTag();
        if (tagCompound != null) {
            setBlacklistMode(tagCompound.getString("blacklistMode"));
            damageMode.setCurrentChoice(tagCompound.getBoolean("damageMode") ? 1 : 0);
            nbtMode.setCurrentChoice(tagCompound.getBoolean("nbtMode") ? 1 : 0);
            modMode.setCurrentChoice(tagCompound.getBoolean("modMode") ? 1 : 0);
        } else {
            setBlacklistMode("White");
        }

        Panel toplevel = positional().background(iconLocation)
                .children(blacklistMode, damageMode, nbtMode, modMode, list, slider, remove, expand, addTags);

        toplevel.bounds(guiLeft, guiTop, xSize, ySize);

        window = new Window(this, toplevel);

        fillList();
    }

    private void addTagWindow() {
        selector.create(window, TagSelectorWindow.TYPE_BOTH, t -> {
            if (t != null) {
                FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
                inventory.addTag(new ResourceLocation(t));
                inventory.markDirty();
                refresh();
            }
        }, () -> null, true);
    }

    private void refresh() {
        syncStack();
        fillList();
    }

    private void expandToTags() {
        if (!canExpand()) {
            return;
        }
        FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
        ItemStack stack = inventory.getStacks().get(list.getSelected() - inventory.getTags().size());

        Set<ResourceLocation> tags = stack.getItem().getTags();
        if (tags.isEmpty()) {
            return;
        }

        removeSelection();

        inventory = new FilterModuleInventory(Minecraft.getInstance().player);
        for (ResourceLocation tag : tags) {
            inventory.addTag(tag);
        }
        inventory.markDirty();
        refresh();
    }

    private void removeSelection() {
        if (!canRemove()) {
            return;
        }
        FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
        if (list.getSelected() >= inventory.getTags().size()) {
            inventory.removeStack(list.getSelected()-inventory.getTags().size());
        } else {
            inventory.removeTag((ResourceLocation)list.getChild(list.getSelected()).getUserObject());
        }
        inventory.markDirty();
        refresh();
    }

    @Override
    protected void drawWindow() {
        super.drawWindow();
        remove.enabled(canRemove());
        expand.enabled(canExpand());
    }

    private boolean canRemove() {
        return list.getSelected() != -1;
    }

    private boolean canExpand() {
        return list.getSelected() != -1 && list.getChild(list.getSelected()).getUserObject() == null;
    }

    private void fillList() {
        FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
        list.removeChildren();

        for (ResourceLocation tag : inventory.getTags()) {
            Tag<Item> itemTag = ItemTags.getCollection().get(tag);
            if (itemTag != null) {
                addTagToList(itemTag);
            } else {
                Tag<Block> blockTag = BlockTags.getCollection().get(tag);
                if (blockTag != null) {
                    addTagToList(blockTag);
                }
            }
        }

        for (ItemStack stack : inventory.getStacks()) {
            Panel panel = horizontal();
            BlockRender render = new BlockRender().renderItem(stack);
            panel.children(render);
            String formattedText = stack.getDisplayName().getFormattedText();
            if (formattedText.length() >= 30) {
                formattedText = formattedText.substring(0, 28) + "...";
            }
            panel.children(label(formattedText));
            list.children(panel);
        }
    }

    private <T extends IItemProvider> void addTagToList(Tag<T> tag) {
        Panel panel = horizontal(0, 0);
        panel.userObject(tag.getId());
        panel.children(label(tag.getId().toString()).desiredWidth(120).horizontalAlignment(HorizontalAlignment.ALIGN_LEFT));
        int i = 5;
        for (T item : tag.getAllElements()) {
            BlockRender render = new BlockRender().renderItem(new ItemStack(item));
            panel.children(render);
            i--;
            if (i <= 0) {
                break;
            }
        }
        list.children(panel);
    }

    private void setBlacklistMode(String mode) {
        int idx = this.blacklistMode.findChoice(mode);
        if (idx == -1) {
            this.blacklistMode.setCurrentChoice("Black");
        } else {
            this.blacklistMode.setCurrentChoice(idx);
        }
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if (slotIn != null && !slotIn.getStack().isEmpty()) {
            FilterModuleInventory inventory = new FilterModuleInventory(minecraft.player);
            if (McJtyLib.proxy.isShiftKeyDown()) {
                for (ResourceLocation tag : slotIn.getStack().getItem().getTags()) {
                    inventory.addTag(tag);
                }
            } else {
                inventory.addStack(slotIn.getStack());
            }
            inventory.markDirty();
            refresh();
        }
    }

    @Override
    public List<String> getTooltipFromItem(ItemStack stack) {
        List<String> list = super.getTooltipFromItem(stack);
        list.add(TextFormatting.GOLD + "Click to add to filter");
        list.add(TextFormatting.GOLD + "Shift-Click to add tags to filter");
        return list;
    }

    private void syncStack() {
        RFToolsBaseMessages.INSTANCE.sendToServer(new PacketSyncHandItem(minecraft.player));
    }

    private void updateSettings() {
        RFToolsBaseMessages.INSTANCE.sendToServer(new PacketUpdateNBTItemFilter(
                TypedMap.builder()
                        .put(new Key<>("blacklistMode", Type.STRING), blacklistMode.getCurrentChoice())
                        .put(new Key<>("damageMode", Type.BOOLEAN), damageMode.getCurrentChoiceIndex() == 1)
                        .put(new Key<>("modMode", Type.BOOLEAN), modMode.getCurrentChoiceIndex() == 1)
                        .put(new Key<>("nbtMode", Type.BOOLEAN), nbtMode.getCurrentChoiceIndex() == 1)
                        .build()));
   }
}
