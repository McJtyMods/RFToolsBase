package mcjty.rftoolsbase.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This packet will update the held item NBT from client to server
 */
public class PacketItemNBTToServer {
    private CompoundNBT tagCompound;

    public void fromBytes(PacketBuffer buf) {
        tagCompound = buf.readCompoundTag();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeCompoundTag(tagCompound);
    }

    public PacketItemNBTToServer() {
    }

    public PacketItemNBTToServer(PacketBuffer buf) {
        fromBytes(buf);
    }

    public PacketItemNBTToServer(CompoundNBT tagCompound) {
        this.tagCompound = tagCompound;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            PlayerEntity player = ctx.getSender();
            ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
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