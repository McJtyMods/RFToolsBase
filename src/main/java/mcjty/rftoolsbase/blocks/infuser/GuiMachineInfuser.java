package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.EnergyBar;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.tileentity.GenericEnergyStorageTileEntity;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static mcjty.lib.tileentity.GenericEnergyStorageTileEntity.getCurrentRF;

public class GuiMachineInfuser extends GenericGuiContainer<MachineInfuserTileEntity, MachineInfuserContainer> {
    public static final int INFUSER_WIDTH = 180;
    public static final int INFUSER_HEIGHT = 152;

    private EnergyBar energyBar;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/infuser.png");



    public GuiMachineInfuser(MachineInfuserTileEntity machineInfuserTileEntity, MachineInfuserContainer container, PlayerInventory inventory) {
        super(RFToolsBase.instance, null /*@todo*/, machineInfuserTileEntity, container, inventory, 0, "infuser");
        GenericEnergyStorageTileEntity.setCurrentRF(machineInfuserTileEntity.getStoredPower());

        xSize = INFUSER_WIDTH;
        ySize = INFUSER_HEIGHT;
    }

    @Override
    public void init() {
        super.init();

        energyBar = new EnergyBar(getMinecraft(), this).setName("energybar").setVertical().setLayoutHint(10, 7, 8, 54).setShowText(false);

        Panel toplevel = new Panel(getMinecraft(), this).setBackground(iconLocation).setLayout(new PositionalLayout()).addChild(energyBar); //.addChild(arrow);
        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);

        initializeFields();

        // @todo
//        tileEntity.requestRfFromServer(RFToolsBase.MODID);
    }

    private void initializeFields() {
        energyBar = window.findChild("energybar");
        energyBar.setMaxValue(tileEntity.getCapacity());
        energyBar.setValue(getCurrentRF());
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        drawWindow();

        energyBar.setValue(GenericEnergyStorageTileEntity.getCurrentRF());

        // @todo
        tileEntity.requestRfFromServer(RFToolsBase.MODID);
    }
}
