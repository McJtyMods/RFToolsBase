package mcjty.rftoolsbase.modules.crafting.network;

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

/**
 * This packet will update the held item components on the server. We use an ItemStack as the parameter
 * but only use the components
 */
public record PacketItemComponentsToServer(ItemStack stack) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "itemnbt");
    public static final CustomPacketPayload.Type<PacketItemComponentsToServer> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketItemComponentsToServer> CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, PacketItemComponentsToServer::stack,
            PacketItemComponentsToServer::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketItemComponentsToServer create(ItemStack stack) {
        return new PacketItemComponentsToServer(stack);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return;
            }
//            if (heldItem.getItem() instanceof ProgramCardItem) {
//                heldItem.applyComponents(components);
            if (heldItem.getItem() instanceof CraftingCardItem) {
                heldItem.applyComponents(stack.getComponents());
            }
        });
    }
}