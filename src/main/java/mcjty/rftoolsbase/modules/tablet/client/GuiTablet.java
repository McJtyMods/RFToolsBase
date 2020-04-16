package mcjty.rftoolsbase.modules.tablet.client;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

import static mcjty.lib.gui.widgets.Widgets.positional;


public class GuiTablet extends GenericGuiContainer<GenericTileEntity, TabletContainer> {
    public static final int TABLET_WIDTH = 180;
    public static final int TABLET_HEIGHT = 188;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/tablet.png");

    public GuiTablet(TabletContainer container, PlayerInventory inventory) {
        super(RFToolsBase.instance, null, container, inventory, /* @todo 1.14 */0, "tablet");
        xSize = TABLET_WIDTH;
        ySize = TABLET_HEIGHT;
    }

    @Override
    public void init() {
        super.init();

        Panel toplevel = positional().background(iconLocation);
        toplevel.bounds(guiLeft, guiTop, xSize, ySize);
        window = new Window(this, toplevel);
    }
}
