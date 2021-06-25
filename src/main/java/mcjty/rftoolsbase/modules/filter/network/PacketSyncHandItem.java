package mcjty.rftoolsbase.modules.filter.network;

import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncHandItem {

    public ItemStack stack;

    public PacketSyncHandItem(PacketBuffer buf) {
        stack = buf.readItem();
    }

    public PacketSyncHandItem(PlayerEntity player) {
        this.stack = player.getItemInHand(Hand.MAIN_HAND);
    }

    protected boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof FilterModuleItem || itemStack.getItem() instanceof TabletItem;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeItem(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            PlayerEntity playerEntity = ctx.getSender();
            ItemStack heldItem = playerEntity.getItemInHand(Hand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return;
            }
            // To avoid people messing with packets
            if (!isValidItem(heldItem)) {
                return;
            }
            if (!isValidItem(stack)) {
                return;
            }
            playerEntity.setItemInHand(Hand.MAIN_HAND, stack);
        });
        ctx.setPacketHandled(true);
    }
}
