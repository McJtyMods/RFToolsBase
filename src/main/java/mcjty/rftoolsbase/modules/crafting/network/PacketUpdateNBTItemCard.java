package mcjty.rftoolsbase.modules.crafting.network;

import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketUpdateNBTItemCard(TypedMap args) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(RFToolsBase.MODID, "update_nbt_item_card");

    public static PacketUpdateNBTItemCard create(FriendlyByteBuf buf) {
        return new PacketUpdateNBTItemCard(TypedMapTools.readArguments(buf));
    }

    public static PacketUpdateNBTItemCard create(TypedMap arguments) {
        return new PacketUpdateNBTItemCard(arguments);
    }

    protected boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof CraftingCardItem;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        TypedMapTools.writeArguments(buf, args);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(playerEntity -> {
                ItemStack heldItem = playerEntity.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldItem.isEmpty()) {
                    return;
                }
                // To avoid people messing with packets
                if (!isValidItem(heldItem)) {
                    return;
                }
                CompoundTag tagCompound = heldItem.getOrCreateTag();
                for (Key<?> akey : args.getKeys()) {
                    String key = akey.name();
                    if (Type.STRING.equals(akey.type())) {
                        tagCompound.putString(key, (String) args.get(akey));
                    } else if (Type.INTEGER.equals(akey.type())) {
                        tagCompound.putInt(key, (Integer) args.get(akey));
                    } else if (Type.LONG.equals(akey.type())) {
                        tagCompound.putLong(key, (Long) args.get(akey));
                    } else if (Type.DOUBLE.equals(akey.type())) {
                        tagCompound.putDouble(key, (Double) args.get(akey));
                    } else if (Type.BOOLEAN.equals(akey.type())) {
                        tagCompound.putBoolean(key, (Boolean) args.get(akey));
                    } else if (Type.BLOCKPOS.equals(akey.type())) {
                        throw new RuntimeException("BlockPos not supported for PacketUpdateNBTItem!");
                    } else if (Type.ITEMSTACK.equals(akey.type())) {
                        throw new RuntimeException("ItemStack not supported for PacketUpdateNBTItem!");
                    } else {
                        throw new RuntimeException(akey.type().getType().getSimpleName() + " not supported for PacketUpdateNBTItem!");
                    }
                }
            });
        });
    }
}