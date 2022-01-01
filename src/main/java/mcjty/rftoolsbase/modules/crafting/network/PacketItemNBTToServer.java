package mcjty.rftoolsbase.modules.crafting.network;

import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This packet will update the held item NBT from client to server
 */
public class PacketItemNBTToServer {
    private CompoundTag tagCompound;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(tagCompound);
    }

    public PacketItemNBTToServer() {
    }

    public PacketItemNBTToServer(FriendlyByteBuf buf) {
        tagCompound = buf.readNbt();
    }

    public PacketItemNBTToServer(CompoundTag tagCompound) {
        this.tagCompound = tagCompound;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return;
            }
//            if (heldItem.getItem() instanceof ProgramCardItem) {
//                heldItem.setTagCompound(tagCompound);
            if (heldItem.getItem() instanceof CraftingCardItem) {
                heldItem.setTag(tagCompound);
            }
        });
        ctx.setPacketHandled(true);
    }
}