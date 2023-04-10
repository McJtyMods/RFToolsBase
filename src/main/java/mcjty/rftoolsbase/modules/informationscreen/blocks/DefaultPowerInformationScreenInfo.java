package mcjty.rftoolsbase.modules.informationscreen.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import mcjty.rftoolsbase.modules.informationscreen.client.DefaultPowerInformationRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.Nonnull;

public class DefaultPowerInformationScreenInfo implements IInformationScreenInfo{

    private final BlockEntity tileEntity;

    public static final Key<Long> ENERGY = new Key<>("energy", Type.LONG);
    public static final Key<Long> MAXENERGY = new Key<>("maxenergy", Type.LONG);

    public DefaultPowerInformationScreenInfo(BlockEntity tileEntity) {
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
        return tileEntity.getCapability(ForgeCapabilities.ENERGY).map(h -> TypedMap.builder()
                .put(ENERGY, (long) h.getEnergyStored())
                .put(MAXENERGY, (long) h.getMaxEnergyStored())
                .build()).orElse(TypedMap.EMPTY);
    }

    @Override
    public void render(int mode, PoseStack matrixStack, MultiBufferSource buffer, @Nonnull TypedMap data, Direction orientation, double scale) {
        if (mode == MODE_POWER) {
            DefaultPowerInformationRenderer.renderDefault(matrixStack, buffer, data, orientation, scale);
        } else {
            DefaultPowerInformationRenderer.renderGraphical(matrixStack, buffer, data, orientation, scale);
        }
    }
}
