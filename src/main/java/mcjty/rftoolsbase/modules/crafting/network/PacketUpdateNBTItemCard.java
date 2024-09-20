package mcjty.rftoolsbase.modules.crafting.network;

import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketUpdateNBTItemCard(TypedMap args) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "update_nbt_item_card");
    public static final CustomPacketPayload.Type<PacketUpdateNBTItemCard> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketUpdateNBTItemCard> CODEC = StreamCodec.composite(
            TypedMap.STREAM_CODEC, PacketUpdateNBTItemCard::args,
            PacketUpdateNBTItemCard::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketUpdateNBTItemCard create(TypedMap arguments) {
        return new PacketUpdateNBTItemCard(arguments);
    }

    protected boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof CraftingCardItem;
    }



    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return;
            }
            // To avoid people messing with packets
            if (!isValidItem(heldItem)) {
                return;
            }
            // @todo 1.21
//            CompoundTag tagCompound = heldItem.getOrCreateTag();
//            for (Key<?> akey : args.getKeys()) {
//                String key = akey.name();
//                if (Type.STRING.equals(akey.type())) {
//                    tagCompound.putString(key, (String) args.get(akey));
//                } else if (Type.INTEGER.equals(akey.type())) {
//                    tagCompound.putInt(key, (Integer) args.get(akey));
//                } else if (Type.LONG.equals(akey.type())) {
//                    tagCompound.putLong(key, (Long) args.get(akey));
//                } else if (Type.DOUBLE.equals(akey.type())) {
//                    tagCompound.putDouble(key, (Double) args.get(akey));
//                } else if (Type.BOOLEAN.equals(akey.type())) {
//                    tagCompound.putBoolean(key, (Boolean) args.get(akey));
//                } else if (Type.BLOCKPOS.equals(akey.type())) {
//                    throw new RuntimeException("BlockPos not supported for PacketUpdateNBTItem!");
//                } else if (Type.ITEMSTACK.equals(akey.type())) {
//                    throw new RuntimeException("ItemStack not supported for PacketUpdateNBTItem!");
//                } else {
//                    throw new RuntimeException(akey.type().getType().getSimpleName() + " not supported for PacketUpdateNBTItem!");
//                }
//            }
        });
    }
}