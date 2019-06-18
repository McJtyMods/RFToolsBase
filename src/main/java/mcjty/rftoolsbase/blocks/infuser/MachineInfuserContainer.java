package mcjty.rftoolsbase.blocks.infuser;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class MachineInfuserContainer extends Container {

    public MachineInfuserContainer(@Nullable ContainerType<?> type, int windowId, PlayerInventory inv, PacketBuffer extraData) {
        super(type, windowId);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
