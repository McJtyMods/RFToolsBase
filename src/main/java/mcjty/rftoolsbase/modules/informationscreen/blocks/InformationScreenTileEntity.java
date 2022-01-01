package mcjty.rftoolsbase.modules.informationscreen.blocks;

import mcjty.lib.tileentity.TickingTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.EnergyTools;
import mcjty.lib.varia.OrientationTools;
import mcjty.rftoolsbase.api.infoscreen.CapabilityInformationScreenInfo;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

import static mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule.TYPE_INFORMATION_SCREEN;

public class InformationScreenTileEntity extends TickingTileEntity {

    private int mode = 0;
    private int cnt = 0;
    private long lastHudTime = 0;

    // Client side information
    private TypedMap clientData;

    public InformationScreenTileEntity(BlockPos pos, BlockState state) {
        super(TYPE_INFORMATION_SCREEN.get(), pos, state);
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
    public void tickServer() {
        cnt--;
        if (cnt <= 0) {
            cnt = 10;
            BlockPos offset = getBlockPos().relative(getBlockOrientation().getOpposite());
            BlockEntity te = level.getBlockEntity(offset);
            if (te != null) {
                te.getCapability(CapabilityInformationScreenInfo.INFORMATION_SCREEN_INFO_CAPABILITY).ifPresent(IInformationScreenInfo::tick);
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
    protected void loadInfo(CompoundTag tagCompound) {
        super.loadInfo(tagCompound);
        CompoundTag info = tagCompound.getCompound("Info");
        mode = info.getByte("mode");
    }

    @Override
    protected void saveInfo(CompoundTag tagCompound) {
        super.saveInfo(tagCompound);
        CompoundTag infoTag = getOrCreateInfo(tagCompound);
        infoTag.putByte("mode", (byte) mode);
    }

    @Override
    public void saveClientDataToNBT(CompoundTag tagCompound) {
        CompoundTag infoTag = getOrCreateInfo(tagCompound);
        infoTag.putByte("mode", (byte) mode);
    }

    @Override
    public void loadClientDataFromNBT(CompoundTag tagCompound) {
        CompoundTag info = tagCompound.getCompound("Info");
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
        BlockEntity te = level.getBlockEntity(offset);
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
