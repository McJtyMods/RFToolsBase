package mcjty.rftoolsbase.compat.jei;

import mcjty.lib.network.NetworkTools;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketSendRecipe {
    private List<ItemStack> stacks;

    public void toBytes(PacketBuffer buf) {
        NetworkTools.writeItemStackList(buf, stacks);
    }

    public PacketSendRecipe() {
    }

    public PacketSendRecipe(PacketBuffer buf) {
        stacks = NetworkTools.readItemStackList(buf);
    }

    public PacketSendRecipe(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            PlayerEntity player = ctx.getSender();
            World world = player.getCommandSenderWorld();
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