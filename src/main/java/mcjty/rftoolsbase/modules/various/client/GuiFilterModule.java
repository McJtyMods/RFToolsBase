package mcjty.rftoolsbase.modules.various.client;

import mcjty.lib.McJtyLib;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.TagSelectorWindow;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.layout.HorizontalLayout;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.various.items.FilterModuleInventory;
import mcjty.rftoolsbase.modules.various.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.various.network.PacketUpdateNBTItemFilter;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.List;
import java.util.Set;


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

        remove = new Button(minecraft, this).setLayoutHint(5, 106, 50, 15).setTooltips("Remove current selection").setText("Remove")
                .addButtonEvent(parent -> removeSelection());
        expand = new Button(minecraft, this).setLayoutHint(5, 121, 50, 15).setTooltips("Expand item to tags").setText("Expand")
                .addButtonEvent(parent -> expandToTags());
        addTags = new Button(minecraft, this).setLayoutHint(5, 137, 50, 15).setTooltips("Add tags").setText("Add tags")
                .addButtonEvent(parent -> addTagWindow());

        blacklistMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(5, 152, 16, 16).setTooltips("Black or whitelist mode").addChoiceEvent((parent, newChoice) -> updateSettings());
        blacklistMode.addChoice("Black", "Blacklist items", guiElements, 14 * 16, 32);
        blacklistMode.addChoice("White", "Whitelist items", guiElements, 15 * 16, 32);

        damageMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(21, 152, 16, 16).setTooltips("Filter ignoring damage").addChoiceEvent((parent, newChoice) -> updateSettings());
        damageMode.addChoice("Off", "Ignore damage", guiElements, 6 * 16, 32);
        damageMode.addChoice("On", "Damage must match", guiElements, 7 * 16, 32);

        nbtMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(5, 168, 16, 16).setTooltips("Filter ignoring NBT").addChoiceEvent((parent, newChoice) -> updateSettings());
        nbtMode.addChoice("Off", "Ignore NBT", guiElements, 8 * 16, 32);
        nbtMode.addChoice("On", "NBT must match", guiElements, 9 * 16, 32);

        modMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(21, 168, 16, 16).setTooltips("Filter ignoring mod").addChoiceEvent((parent, newChoice) -> updateSettings());
        modMode.addChoice("Off", "Don't match on mod", guiElements, 12 * 16, 32);
        modMode.addChoice("On", "Only mod must match", guiElements, 13 * 16, 32);

        list = new WidgetList(minecraft, this).setLayoutHint(5, 4, 207, 99).setName("list");
        slider = new Slider(minecraft, this).setLayoutHint(212, 4, 10, 99).setScrollableName("list");

        CompoundNBT tagCompound = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getTag();
        if (tagCompound != null) {
            setBlacklistMode(tagCompound.getString("blacklistMode"));
            damageMode.setCurrentChoice(tagCompound.getBoolean("damageMode") ? 1 : 0);
            nbtMode.setCurrentChoice(tagCompound.getBoolean("nbtMode") ? 1 : 0);
            modMode.setCurrentChoice(tagCompound.getBoolean("modMode") ? 1 : 0);
        }

        Panel toplevel = new Panel(minecraft, this).setLayout(new PositionalLayout()).setBackground(iconLocation)
                .addChildren(blacklistMode, damageMode, nbtMode, modMode, list, slider, remove, expand, addTags);

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);

        fillList();
    }

    private void addTagWindow() {
        selector.create(window, 10, 10, "both", t -> {
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
        remove.setEnabled(canRemove());
        expand.setEnabled(canExpand());
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
                Panel panel = new Panel(minecraft, this).setLayout(new HorizontalLayout().setHorizontalMargin(0).setSpacing(0));
                panel.setUserObject(itemTag.getId());
                panel.addChild(new Label(minecraft, this).setText(tag.toString()).setDesiredWidth(120).setHorizontalAlignment(HorizontalAlignment.ALIGN_LEFT));
                int i = 5;
                for (Item item : itemTag.getAllElements()) {
                    BlockRender render = new BlockRender(minecraft, this).setRenderItem(new ItemStack(item));
                    panel.addChild(render);
                    i--;
                    if (i <= 0) {
                        break;
                    }
                }
                list.addChild(panel);
            }
        }

        for (ItemStack stack : inventory.getStacks()) {
            Panel panel = new Panel(minecraft, this).setLayout(new HorizontalLayout());
            BlockRender render = new BlockRender(minecraft, this).setRenderItem(stack);
            panel.addChild(render);
            String formattedText = stack.getDisplayName().getFormattedText();
            if (formattedText.length() >= 30) {
                formattedText = formattedText.substring(0, 28) + "...";
            }
            panel.addChild(new Label(minecraft, this).setText(formattedText));
            list.addChild(panel);
        }
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
