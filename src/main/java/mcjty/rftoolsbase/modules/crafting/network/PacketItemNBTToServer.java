package mcjty.rftoolsbase.modules.crafting.network;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

/**
 * This packet will update the held item NBT from client to server
 */
public record PacketItemNBTToServer(CompoundTag tagCompound) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(RFToolsBase.MODID, "itemnbt");

    public static PacketItemNBTToServer create(FriendlyByteBuf buf) {
        return new PacketItemNBTToServer(buf.readNbt());
    }

    public static PacketItemNBTToServer create(CompoundTag tagCompound) {
        return new PacketItemNBTToServer(tagCompound);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(tagCompound);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> {
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
        });
    }
}