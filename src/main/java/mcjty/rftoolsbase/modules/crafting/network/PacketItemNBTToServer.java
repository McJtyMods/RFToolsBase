package mcjty.rftoolsbase.modules.crafting.network;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * This packet will update the held item NBT from client to server
 */
public record PacketItemNBTToServer(CompoundTag tagCompound) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "itemnbt");
    public static final CustomPacketPayload.Type<PacketItemNBTToServer> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, PacketItemNBTToServer> CODEC = StreamCodec.composite(
            ByteBufCodecs.compoundTagCodec(NbtAccounter::unlimitedHeap), PacketItemNBTToServer::tagCompound,
            PacketItemNBTToServer::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketItemNBTToServer create(CompoundTag tagCompound) {
        return new PacketItemNBTToServer(tagCompound);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return;
            }
//            if (heldItem.getItem() instanceof ProgramCardItem) {
//                heldItem.setTagCompound(tagCompound);
            if (heldItem.getItem() instanceof CraftingCardItem) {
                // @todo 1.21
//                heldItem.setTag(tagCompound);
            }
        });
    }
}