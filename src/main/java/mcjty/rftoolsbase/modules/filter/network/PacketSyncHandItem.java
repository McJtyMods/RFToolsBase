package mcjty.rftoolsbase.modules.filter.network;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSyncHandItem(ItemStack stack) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "synchanditem");
    public static final CustomPacketPayload.Type<PacketSyncHandItem> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncHandItem> CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, PacketSyncHandItem::stack,
            PacketSyncHandItem::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketSyncHandItem create(Player player) {
        return new PacketSyncHandItem(player.getItemInHand(InteractionHand.MAIN_HAND));
    }

    protected boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof FilterModuleItem || itemStack.getItem() instanceof TabletItem;
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
            if (!isValidItem(stack)) {
                return;
            }
            // Do not use the client stack directly. Keep the server item and copy only the data that the gui changes
            if (heldItem.getItem() instanceof TabletItem) {
                if (stack.getItem() instanceof TabletItem) {
                    int current = Mth.clamp(TabletItem.getCurrentSlot(stack), 0, TabletContainer.NUM_SLOTS - 1);
                    ItemStack containingItem = TabletItem.getContainingItem(heldItem, current);
                    if (containingItem.isEmpty() || containingItem.getItem() instanceof ITabletSupport) {
                        TabletItem.setCurrentSlot(player, heldItem, current);
                    }
                }
            } else if (stack.getItem() == heldItem.getItem()) {
                ItemStack newStack = heldItem.copy();
                newStack.set(FilterModule.ITEM_FILTERMODULE_DATA, stack.get(FilterModule.ITEM_FILTERMODULE_DATA));
                player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
            }
        });
    }
}
