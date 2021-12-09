package mcjty.rftoolsbase.modules.informationscreen.blocks;

import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.EnergyTools;
import mcjty.lib.varia.OrientationTools;
import mcjty.rftoolsbase.api.infoscreen.CapabilityInformationScreenInfo;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;

import static mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule.TYPE_INFORMATION_SCREEN;

public class InformationScreenTileEntity extends GenericTileEntity implements ITickableTileEntity {

    private int mode = 0;
    private int cnt = 0;
    private long lastHudTime = 0;

    // Client side information
    private TypedMap clientData;

    public InformationScreenTileEntity() {
        super(TYPE_INFORMATION_SCREEN.get());
    }

    public Direction getBlockOrientation() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getBlock() instanceof InformationScreenBlock) {
            return OrientationTools.getOrientationHoriz(state);
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            cnt--;
            if (cnt <= 0) {
                cnt = 10;
                BlockPos offset = getBlockPos().relative(getBlockOrientation().getOpposite());
                TileEntity te = level.getBlockEntity(offset);
                if (te != null) {
                    te.getCapability(CapabilityInformationScreenInfo.INFORMATION_SCREEN_INFO_CAPABILITY).ifPresent(IInformationScreenInfo::tick);
                }
            }
        }
    }

    public void toggleMode() {
        getInfo().ifPresent(h -> {
            int[] modes = h.getSupportedModes();
            int found = -1;
            for (int i = 0 ; i < modes.length ; i++) {
                if (modes[i] == mode) {
                    found = i;
                    break;
                }
            }
            if (found != -1) {
                found++;
                mode = modes[found % modes.length];
                markDirtyClient();
            }
        });
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected void loadInfo(CompoundNBT tagCompound) {
        super.loadInfo(tagCompound);
        CompoundNBT info = tagCompound.getCompound("Info");
        mode = info.getByte("mode");
    }

    @Override
    protected void saveInfo(CompoundNBT tagCompound) {
        super.saveInfo(tagCompound);
        CompoundNBT infoTag = getOrCreateInfo(tagCompound);
        infoTag.putByte("mode", (byte) mode);
    }

    @Override
    public void writeClientDataToNBT(CompoundNBT tagCompound) {
        CompoundNBT infoTag = getOrCreateInfo(tagCompound);
        infoTag.putByte("mode", (byte) mode);
    }

    @Override
    public void readClientDataFromNBT(CompoundNBT tagCompound) {
        CompoundNBT info = tagCompound.getCompound("Info");
        mode = info.getByte("mode");
    }

    public void setClientData(TypedMap power) {
        this.clientData = power;
    }

    public TypedMap getClientData() {
        return clientData;
    }

    public long getLastUpdateTime() {
        return lastHudTime;
    }

    public void setLastUpdateTime(long t) {
        lastHudTime = t;
    }

    public LazyOptional<IInformationScreenInfo> getInfo() {
        BlockPos offset = getBlockPos().relative(getBlockOrientation().getOpposite());
        TileEntity te = level.getBlockEntity(offset);
        if (te != null) {
            LazyOptional<IInformationScreenInfo> capability = te.getCapability(CapabilityInformationScreenInfo.INFORMATION_SCREEN_INFO_CAPABILITY);
            if (capability.isPresent()) {
                return capability.cast();
            }
            if (EnergyTools.isEnergyTE(te, getBlockOrientation())) {
                return LazyOptional.of(() -> new DefaultPowerInformationScreenInfo(te));
            }
        }
        return LazyOptional.empty();
    }
}
