package mcjty.rftoolsbase.compat.jei;

import mcjty.lib.network.NetworkTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record PacketSendRecipe(List<ItemStack> stacks) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(RFToolsBase.MODID, "sendrecipe");

    public static PacketSendRecipe create(FriendlyByteBuf buf) {
        return new PacketSendRecipe(NetworkTools.readItemStackList(buf));
    }

    public static PacketSendRecipe create(List<ItemStack> stacks) {
        return new PacketSendRecipe(stacks);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        NetworkTools.writeItemStackList(buf, stacks);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> {
                // Handle tablet version
                ItemStack mainhand = player.getMainHandItem();
                if (!mainhand.isEmpty() && mainhand.getItem() == CraftingModule.CRAFTING_CARD.get()) {
                    if (player.containerMenu instanceof CraftingCardContainer craftingCardContainer) {
                        craftingCardContainer.setGridContents(player, stacks);
                    }
                }
            });
        });
    }
}