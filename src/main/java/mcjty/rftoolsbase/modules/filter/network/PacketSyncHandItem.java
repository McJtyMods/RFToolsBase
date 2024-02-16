package mcjty.rftoolsbase.modules.filter.network;

import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record PacketSyncHandItem(ItemStack stack) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(RFToolsBase.MODID, "synchanditem");

    public static PacketSyncHandItem create(FriendlyByteBuf buf) {
        return new PacketSyncHandItem(buf.readItem());
    }

    public static PacketSyncHandItem create(Player player) {
        return new PacketSyncHandItem(player.getItemInHand(InteractionHand.MAIN_HAND));
    }

    protected boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof FilterModuleItem || itemStack.getItem() instanceof TabletItem;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(stack);
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
                if (!isValidItem(stack)) {
                    return;
                }
                playerEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);
            });
        });
    }
}
