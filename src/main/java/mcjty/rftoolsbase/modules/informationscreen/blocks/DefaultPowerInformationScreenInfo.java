package mcjty.rftoolsbase.modules.informationscreen.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import mcjty.rftoolsbase.modules.informationscreen.client.DefaultPowerInformationRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;

public class DefaultPowerInformationScreenInfo implements IInformationScreenInfo{

    private final TileEntity tileEntity;

    public static final Key<Long> ENERGY = new Key<>("energy", Type.LONG);
    public static final Key<Long> MAXENERGY = new Key<>("maxenergy", Type.LONG);

    public DefaultPowerInformationScreenInfo(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public int[] getSupportedModes() {
        return new int[]{ MODE_POWER, MODE_POWER_GRAPHICAL };
    }

    @Override
    public void tick() {

    }

    @Nonnull
    @Override
    public TypedMap getInfo(int mode) {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(h -> {
            return TypedMap.builder()
                    .put(ENERGY, (long) h.getEnergyStored())
                    .put(MAXENERGY, (long) h.getMaxEnergyStored())
                    .build();
        }).orElse(TypedMap.EMPTY);
    }

    @Override
    public void render(int mode, MatrixStack matrixStack, IRenderTypeBuffer buffer, @Nonnull TypedMap data, Direction orientation, double x, double y, double z, double scale) {
        if (mode == MODE_POWER) {
            DefaultPowerInformationRenderer.renderDefault(matrixStack, buffer, data, orientation, x, y, z, scale);
        } else {
            DefaultPowerInformationRenderer.renderGraphical(matrixStack, buffer, data, orientation, x, y, z, scale);
        }
    }
}
