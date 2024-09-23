package mcjty.rftoolsbase.modules.filter.client;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.TagSelectorWindow;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.SafeClientTools;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleInventory;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.filter.network.PacketUpdateNBTItemFilter;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

import static mcjty.lib.gui.widgets.Widgets.*;


public class GuiFilterModule extends GenericGuiContainer<GenericTileEntity, FilterModuleContainer> {
    public static final int CONTROLLER_WIDTH = 230;
    public static final int CONTROLLER_HEIGHT = 188;

    private static final ResourceLocation iconLocation = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "textures/gui/filtermodule.png");
    private static final ResourceLocation guiElements = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "textures/gui/guielements.png");

    private final TagSelectorWindow selector = new TagSelectorWindow();

    private ImageChoiceLabel blacklistMode;
    private ImageChoiceLabel damageMode;
    private ImageChoiceLabel nbtMode;
    private ImageChoiceLabel modMode;

    private Button remove;
    private Button expand;

    private WidgetList list;

    public GuiFilterModule(FilterModuleContainer container, Inventory inventory, Component title) {
        super(container, inventory, title, FilterModuleItem.MANUAL);
        imageWidth = CONTROLLER_WIDTH;
        imageHeight = CONTROLLER_HEIGHT;
    }

    public static void register(RegisterMenuScreensEvent event) {
        event.register(FilterModule.CONTAINER_FILTER_MODULE.get(), GuiFilterModule::new);
    }

    @Override
    public void init() {
        super.init();

        remove = button(5, 106, 50, 15, "Remove").tooltips("Remove current selection").event(this::removeSelection);
        expand = button(5, 121, 50, 15, "Expand").tooltips("Expand item to tags").event(this::expandToTags);
        Button addTags = button(5, 137, 50, 15, "Add tags").tooltips("Add tags").event(this::addTagWindow);

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
        Slider slider = slider(212, 4, 10, 99).scrollableName("list");

        // @todo 1.21
//        CompoundTag tagCompound = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getTag();
//        if (tagCompound != null) {
//            setBlacklistMode(tagCompound.getString("blacklistMode"));
//            damageMode.setCurrentChoice(tagCompound.getBoolean("damageMode") ? 1 : 0);
//            nbtMode.setCurrentChoice(tagCompound.getBoolean("nbtMode") ? 1 : 0);
//            modMode.setCurrentChoice(tagCompound.getBoolean("modMode") ? 1 : 0);
//        } else {
//            setBlacklistMode("White");
//        }

        Panel toplevel = positional().background(iconLocation)
                .children(blacklistMode, damageMode, nbtMode, modMode, list, slider, remove, expand, addTags);

        toplevel.bounds(leftPos, topPos, imageWidth, imageHeight);

        window = new Window(this, toplevel);

        fillList();
    }

    private void addTagWindow() {
        selector.create(window, TagSelectorWindow.TYPE_BOTH, t -> {
            if (t != null) {
                FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
                inventory.addTag(TagTools.createItemTagKey(ResourceLocation.parse(t)));
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

        Collection<TagKey<Item>> tags = TagTools.getTags(stack.getItem());
        if (tags.isEmpty()) {
            return;
        }

        removeSelection();

        inventory = new FilterModuleInventory(Minecraft.getInstance().player);
        for (TagKey<Item> tag : tags) {
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
            ResourceLocation location = (ResourceLocation) list.getChild(list.getSelected()).getUserObject();
            TagKey<Item> tag = TagTools.createItemTagKey(location);
            inventory.removeTag(tag);
        }
        inventory.markDirty();
        refresh();
    }

    @Override
    protected void drawWindow(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.drawWindow(graphics, partialTicks, x, y);
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

        for (TagKey<Item> tag : inventory.getTags()) {
            addTagToList(tag);
        }

        for (ItemStack stack : inventory.getStacks()) {
            Panel panel = horizontal();
            BlockRender render = new BlockRender().renderItem(stack);
            panel.children(render);
            String formattedText = stack.getHoverName().getString() /* was getFormattedText() */;
            if (formattedText.length() >= 30) {
                formattedText = formattedText.substring(0, 28) + "...";
            }
            panel.children(label(formattedText));
            list.children(panel);
        }
    }

    private void addTagToList(TagKey<Item> tag) {
        Panel panel = horizontal(0, 0);
        panel.userObject(tag.location());
        panel.children(label(tag.location().toString()).desiredWidth(120).horizontalAlignment(HorizontalAlignment.ALIGN_LEFT));
        int i = 5;
        for (Holder<Item> item : TagTools.getItemsForTag(tag)) {
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
    protected void slotClicked(Slot slotIn, int slotId, int mouseButton, @Nonnull ClickType type) {
        if (slotIn == null) {
            return;
        }
        if (!slotIn.getItem().isEmpty()) {
            FilterModuleInventory inventory = new FilterModuleInventory(minecraft.player);
            if (SafeClientTools.isSneaking()) {
                TagTools.getTags(slotIn.getItem().getItem()).forEach(inventory::addTag);
            } else {
                inventory.addStack(slotIn.getItem());
            }
            inventory.markDirty();
            refresh();
        }
    }

    @Override
    protected List<Component> getTooltipFromContainerItem(ItemStack stack) {
        List<Component> list = getTooltipFromItem(minecraft, stack);
        list.add(ComponentFactory.literal(ChatFormatting.GOLD + "Click to add to filter"));
        list.add(ComponentFactory.literal(ChatFormatting.GOLD + "Shift-Click to add tags to filter"));
        return list;
    }

    private void syncStack() {
        RFToolsBaseMessages.sendToServer(PacketSyncHandItem.create(minecraft.player));
    }

    private void updateSettings() {
        RFToolsBaseMessages.sendToServer(PacketUpdateNBTItemFilter.create(
                TypedMap.builder()
                        .put(new Key<>("blacklistMode", Type.STRING), blacklistMode.getCurrentChoice())
                        .put(new Key<>("damageMode", Type.BOOLEAN), damageMode.getCurrentChoiceIndex() == 1)
                        .put(new Key<>("modMode", Type.BOOLEAN), modMode.getCurrentChoiceIndex() == 1)
                        .put(new Key<>("nbtMode", Type.BOOLEAN), nbtMode.getCurrentChoiceIndex() == 1)
                        .build()));
   }
}
