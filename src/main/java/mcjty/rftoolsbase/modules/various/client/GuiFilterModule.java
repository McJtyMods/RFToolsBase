package mcjty.rftoolsbase.modules.various.client;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.layout.HorizontalLayout;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.various.items.FilterModuleInventory;
import mcjty.rftoolsbase.modules.various.network.PacketUpdateNBTItemFilter;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.awt.*;


public class GuiFilterModule extends GenericGuiContainer<GenericTileEntity, FilterModuleContainer> {
    public static final int CONTROLLER_WIDTH = 230;
    public static final int CONTROLLER_HEIGHT = 188;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/filtermodule.png");
    private static final ResourceLocation guiElements = new ResourceLocation(RFToolsBase.MODID, "textures/gui/guielements.png");

    private ImageChoiceLabel blacklistMode;
    private ImageChoiceLabel commonTagMode;
    private ImageChoiceLabel damageMode;
    private ImageChoiceLabel nbtMode;
    private ImageChoiceLabel modMode;

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

        blacklistMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(5, 106, 16, 16).setTooltips("Black or whitelist mode").addChoiceEvent((parent, newChoice) -> updateSettings());
        blacklistMode.addChoice("Black", "Blacklist items", guiElements, 14 * 16, 32);
        blacklistMode.addChoice("White", "Whitelist items", guiElements, 15 * 16, 32);

        commonTagMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(21, 106, 16, 16).setTooltips("Filter based on common tags").addChoiceEvent((parent, newChoice) -> updateSettings());
        commonTagMode.addChoice("Off", "Common tag matching off", guiElements, 10 * 16, 32);
        commonTagMode.addChoice("On", "Common tag matching on", guiElements, 11 * 16, 32);

        damageMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(5, 124, 16, 16).setTooltips("Filter ignoring damage").addChoiceEvent((parent, newChoice) -> updateSettings());
        damageMode.addChoice("Off", "Ignore damage", guiElements, 6 * 16, 32);
        damageMode.addChoice("On", "Damage must match", guiElements, 7 * 16, 32);

        nbtMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(21, 124, 16, 16).setTooltips("Filter ignoring NBT").addChoiceEvent((parent, newChoice) -> updateSettings());
        nbtMode.addChoice("Off", "Ignore NBT", guiElements, 8 * 16, 32);
        nbtMode.addChoice("On", "NBT must match", guiElements, 9 * 16, 32);

        modMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(5, 142, 16, 16).setTooltips("Filter ignoring mod").addChoiceEvent((parent, newChoice) -> updateSettings());
        modMode.addChoice("Off", "Don't match on mod", guiElements, 12 * 16, 32);
        modMode.addChoice("On", "Only mod must match", guiElements, 13 * 16, 32);

        list = new WidgetList(minecraft, this).setLayoutHint(5, 4, 207, 99).setName("list");
        slider = new Slider(minecraft, this).setLayoutHint(212, 4, 10, 99).setScrollableName("list");

        CompoundNBT tagCompound = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getTag();
        if (tagCompound != null) {
            setBlacklistMode(tagCompound.getString("blacklistMode"));
            commonTagMode.setCurrentChoice(tagCompound.getBoolean("commonTagMode") ? 1 : 0);
            damageMode.setCurrentChoice(tagCompound.getBoolean("damageMode") ? 1 : 0);
            nbtMode.setCurrentChoice(tagCompound.getBoolean("nbtMode") ? 1 : 0);
            modMode.setCurrentChoice(tagCompound.getBoolean("modMode") ? 1 : 0);
        }

        Panel toplevel = new Panel(minecraft, this).setLayout(new PositionalLayout()).setBackground(iconLocation)
                .addChildren(blacklistMode, commonTagMode, damageMode, nbtMode, modMode, list, slider);

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);

        fillList();
    }

    private void fillList() {
        FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
        list.removeChildren();
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
        addDummy(list, BlockTags.LOGS.getId().toString(), new ItemStack(Blocks.OAK_LOG), new ItemStack(Blocks.SPRUCE_LOG),
                new ItemStack(Blocks.BIRCH_LOG), new ItemStack(Blocks.ACACIA_LOG), new ItemStack(Blocks.DARK_OAK_LOG));
        addDummy(list, Tags.Blocks.ORES.getId().toString(), new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.GOLD_ORE),
                new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Blocks.DIAMOND_ORE));
    }

    private void addDummy(WidgetList list, String txt, ItemStack... stacks) {
        Panel panel = new Panel(minecraft, this).setLayout(new HorizontalLayout());
        panel.addChild(new Label(minecraft, this).setText(txt).setDesiredWidth(90).setHorizontalAlignment(HorizontalAlignment.ALIGN_LEFT));
        for (ItemStack stack : stacks) {
            BlockRender render = new BlockRender(minecraft, this).setRenderItem(stack);
            panel.addChild(render);
        }
        list.addChild(panel);
    }

    private void setBlacklistMode(String mode) {
        int idx = this.blacklistMode.findChoice(mode);
        if (idx == -1) {
            this.blacklistMode.setCurrentChoice("Black");
        } else {
            this.blacklistMode.setCurrentChoice(idx);
        }
    }

    private void updateSettings() {
        RFToolsBaseMessages.INSTANCE.sendToServer(new PacketUpdateNBTItemFilter(
                TypedMap.builder()
                        .put(new Key<>("blacklistMode", Type.STRING), blacklistMode.getCurrentChoice())
                        .put(new Key<>("commonTagMode", Type.BOOLEAN), commonTagMode.getCurrentChoiceIndex() == 1)
                        .put(new Key<>("damageMode", Type.BOOLEAN), damageMode.getCurrentChoiceIndex() == 1)
                        .put(new Key<>("modMode", Type.BOOLEAN), modMode.getCurrentChoiceIndex() == 1)
                        .put(new Key<>("nbtMode", Type.BOOLEAN), nbtMode.getCurrentChoiceIndex() == 1)
                        .build()));
   }
}
