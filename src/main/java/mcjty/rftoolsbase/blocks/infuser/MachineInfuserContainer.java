package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class MachineInfuserContainer extends GenericContainer {

    private final BlockPos pos;

    public MachineInfuserContainer(@Nullable ContainerType<?> type, int windowId, PlayerInventory inv, BlockPos pos) {
        super(type, windowId, MachineInfuserTileEntity.CONTAINER_FACTORY);
        addInventory(ContainerFactory.CONTAINER_PLAYER, inv);
        this.pos = pos;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }   // @todo distance

    public BlockPos getPos() {
        return pos;
    }
}
