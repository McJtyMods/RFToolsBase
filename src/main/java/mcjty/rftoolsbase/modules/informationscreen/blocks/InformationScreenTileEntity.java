package mcjty.rftoolsbase.modules.informationscreen.blocks;

import mcjty.lib.tileentity.TickingTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.EnergyTools;
import mcjty.lib.varia.OrientationTools;
import mcjty.rftoolsbase.api.infoscreen.CapabilityInformationScreenInfo;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

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
            IInformationScreenInfo capability = level.getCapability(CapabilityInformationScreenInfo.INFORMATION_SCREEN_INFO_CAPABILITY, offset, null);
            if (capability != null) {
                capability.tick();
            }
        }
    }

    public void toggleMode() {
        IInformationScreenInfo h = getInfo();
        int[] modes = h.getSupportedModes();
        int found = -1;
        for (int i = 0; i < modes.length; i++) {
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
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putByte("mode", (byte) mode);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        mode = tag.getByte("mode");
    }

    @Override
    public void saveClientDataToNBT(CompoundTag tag) {
        tag.putByte("mode", (byte) mode);
    }

    @Override
    public void loadClientDataFromNBT(CompoundTag tag) {
        mode = tag.getByte("mode");
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

    @Nullable
    public IInformationScreenInfo getInfo() {
        BlockPos offset = getBlockPos().relative(getBlockOrientation().getOpposite());
        BlockEntity te = level.getBlockEntity(offset);
        if (te != null) {
            IInformationScreenInfo capability = level.getCapability(CapabilityInformationScreenInfo.INFORMATION_SCREEN_INFO_CAPABILITY, offset, null);
            if (capability != null) {
                return capability;
            }
            if (EnergyTools.isEnergyTE(te, getBlockOrientation())) {
                return new DefaultPowerInformationScreenInfo(te);
            }
        }
        return null;
    }
}
