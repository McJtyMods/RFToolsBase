package mcjty.rftoolsbase.modules.various.client;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.ImageChoiceLabel;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.various.network.PacketUpdateNBTItemFilter;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import java.awt.*;


public class GuiFilterModule extends GenericGuiContainer<GenericTileEntity, FilterModuleContainer> {
    public static final int CONTROLLER_WIDTH = 180;
    public static final int CONTROLLER_HEIGHT = 188;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/filtermodule.png");
    private static final ResourceLocation guiElements = new ResourceLocation(RFToolsBase.MODID, "textures/gui/guielements.png");

    private ImageChoiceLabel blacklistMode;
    private ImageChoiceLabel commonTagMode;
    private ImageChoiceLabel damageMode;
    private ImageChoiceLabel nbtMode;
    private ImageChoiceLabel modMode;

    public GuiFilterModule(FilterModuleContainer container, PlayerInventory inventory) {
        super(RFToolsBase.instance, null, container, inventory, /* @todo 1.14 */0, "storfilter");
        xSize = CONTROLLER_WIDTH;
        ySize = CONTROLLER_HEIGHT;
    }

    @Override
    public void init() {
        super.init();

        blacklistMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(130, 9, 16, 16).setTooltips("Black or whitelist mode").addChoiceEvent((parent, newChoice) -> updateSettings());
        blacklistMode.addChoice("Black", "Blacklist items", guiElements, 14 * 16, 32);
        blacklistMode.addChoice("White", "Whitelist items", guiElements, 15 * 16, 32);

        commonTagMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(148, 9, 16, 16).setTooltips("Filter based on common tags").addChoiceEvent((parent, newChoice) -> updateSettings());
        commonTagMode.addChoice("Off", "Common tag matching off", guiElements, 10 * 16, 32);
        commonTagMode.addChoice("On", "Common tag matching on", guiElements, 11 * 16, 32);

        damageMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(130, 27, 16, 16).setTooltips("Filter ignoring damage").addChoiceEvent((parent, newChoice) -> updateSettings());
        damageMode.addChoice("Off", "Ignore damage", guiElements, 6 * 16, 32);
        damageMode.addChoice("On", "Damage must match", guiElements, 7 * 16, 32);

        nbtMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(148, 27, 16, 16).setTooltips("Filter ignoring NBT").addChoiceEvent((parent, newChoice) -> updateSettings());
        nbtMode.addChoice("Off", "Ignore NBT", guiElements, 8 * 16, 32);
        nbtMode.addChoice("On", "NBT must match", guiElements, 9 * 16, 32);

        modMode = new ImageChoiceLabel(minecraft, this).setLayoutHint(130, 45, 16, 16).setTooltips("Filter ignoring mod").addChoiceEvent((parent, newChoice) -> updateSettings());
        modMode.addChoice("Off", "Don't match on mod", guiElements, 12 * 16, 32);
        modMode.addChoice("On", "Only mod must match", guiElements, 13 * 16, 32);

        CompoundNBT tagCompound = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getTag();
        if (tagCompound != null) {
            setBlacklistMode(tagCompound.getString("blacklistMode"));
            commonTagMode.setCurrentChoice(tagCompound.getBoolean("commonTagMode") ? 1 : 0);
            damageMode.setCurrentChoice(tagCompound.getBoolean("damageMode") ? 1 : 0);
            nbtMode.setCurrentChoice(tagCompound.getBoolean("nbtMode") ? 1 : 0);
            modMode.setCurrentChoice(tagCompound.getBoolean("modMode") ? 1 : 0);
        }

        Panel toplevel = new Panel(minecraft, this).setLayout(new PositionalLayout()).setBackground(iconLocation)
                .addChildren(blacklistMode, commonTagMode, damageMode, nbtMode, modMode);

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);
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
