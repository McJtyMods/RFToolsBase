package mcjty.rftoolsbase.compat.jei;

import mcjty.lib.network.NetworkTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record PacketSendRecipe(List<ItemStack> stacks) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "sendrecipe");
    public static final CustomPacketPayload.Type<PacketSendRecipe> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSendRecipe> CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, PacketSendRecipe::stacks,
            PacketSendRecipe::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketSendRecipe create(List<ItemStack> stacks) {
        return new PacketSendRecipe(stacks);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            // Handle tablet version
            ItemStack mainhand = player.getMainHandItem();
            if (!mainhand.isEmpty() && mainhand.getItem() == CraftingModule.CRAFTING_CARD.get()) {
                if (player.containerMenu instanceof CraftingCardContainer craftingCardContainer) {
                    craftingCardContainer.setGridContents(player, stacks);
                }
            }
        });
    }
}