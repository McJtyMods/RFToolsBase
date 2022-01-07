package mcjty.rftoolsbase.modules.filter.network;

import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncHandItem {

    private final ItemStack stack;

    public PacketSyncHandItem(FriendlyByteBuf buf) {
        stack = buf.readItem();
    }

    public PacketSyncHandItem(Player player) {
        this.stack = player.getItemInHand(InteractionHand.MAIN_HAND);
    }

    protected boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof FilterModuleItem || itemStack.getItem() instanceof TabletItem;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Player playerEntity = ctx.getSender();
            ItemStack heldItem = playerEntity.getItemInHand(InteractionHand.MAIN_HAND);
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
            playerEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);
        });
        ctx.setPacketHandled(true);
    }
}
