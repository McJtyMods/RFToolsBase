package mcjty.rftoolsbase.modules.filter.network;

import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateNBTItemFilter {

    private final TypedMap args;

    public PacketUpdateNBTItemFilter(FriendlyByteBuf buf) {
        args = TypedMapTools.readArguments(buf);
    }

    public PacketUpdateNBTItemFilter(TypedMap arguments) {
        this.args = arguments;
    }

    protected boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof FilterModuleItem;
    }

    public void toBytes(FriendlyByteBuf buf) {
        TypedMapTools.writeArguments(buf, args);
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
        ctx.setPacketHandled(true);
    }
}
