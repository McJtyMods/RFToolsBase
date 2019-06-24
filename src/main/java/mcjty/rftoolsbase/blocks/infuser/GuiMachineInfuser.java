package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.container.GenericContainer;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.EnergyBar;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.tileentity.GenericEnergyStorage;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;

import java.awt.Rectangle;

public class GuiMachineInfuser extends GenericGuiContainer<MachineInfuserTileEntity, GenericContainer> {
    public static final int INFUSER_WIDTH = 180;
    public static final int INFUSER_HEIGHT = 152;

    private EnergyBar energyBar;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFToolsBase.MODID, "textures/gui/infuser.png");

    public GuiMachineInfuser(MachineInfuserTileEntity machineInfuserTileEntity, GenericContainer container, PlayerInventory inventory) {
        super(RFToolsBase.instance, null /*@todo*/, machineInfuserTileEntity, container, inventory, 0, "infuser");

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
    }

    private void initializeFields() {
        energyBar = window.findChild("energybar");
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        drawWindow();

        tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
            energyBar.setMaxValue(((GenericEnergyStorage)e).getCapacity());
            energyBar.setValue(((GenericEnergyStorage)e).getEnergy());
        });
    }
}
