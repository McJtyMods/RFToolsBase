package mcjty.rftoolsbase.blocks.infuser;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class MachineInfuserContainer extends Container {

    private final BlockPos pos;

    public MachineInfuserContainer(@Nullable ContainerType<?> type, int windowId, PlayerInventory inv, PacketBuffer extraData) {
        super(type, windowId);
        this.pos = extraData.readBlockPos();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }   // @todo distance

    public BlockPos getPos() {
        return pos;
    }
}
