package mcjty.rftoolsbase.compat.jei;

import mcjty.lib.network.NetworkTools;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketSendRecipe {
    private List<ItemStack> stacks;

    public void toBytes(FriendlyByteBuf buf) {
        NetworkTools.writeItemStackList(buf, stacks);
    }

    public PacketSendRecipe() {
    }

    public PacketSendRecipe(FriendlyByteBuf buf) {
        stacks = NetworkTools.readItemStackList(buf);
    }

    public PacketSendRecipe(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            Level world = player.getCommandSenderWorld();
            // Handle tablet version
            ItemStack mainhand = player.getMainHandItem();
            if (!mainhand.isEmpty() && mainhand.getItem() == CraftingModule.CRAFTING_CARD.get()) {
                if (player.containerMenu instanceof CraftingCardContainer) {
                    CraftingCardContainer craftingCardContainer = (CraftingCardContainer) player.containerMenu;
                    craftingCardContainer.setGridContents(player, stacks);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}