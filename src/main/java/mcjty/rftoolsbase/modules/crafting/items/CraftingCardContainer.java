package mcjty.rftoolsbase.modules.crafting.items;

import mcjty.lib.container.*;
import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;

public class CraftingCardContainer extends GenericContainer {

    public static final int SLOT_INPUT = 0;
    public static final int GRID_WIDTH = 5;
    public static final int GRID_HEIGHT = 4;
    public static final int INPUT_SLOTS = GRID_WIDTH * GRID_HEIGHT;
    public static final int SLOT_OUT = INPUT_SLOTS;

    private int cardIndex;

    public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(0)
            .playerSlots(10, 116));

    public CraftingCardContainer(int id, BlockPos pos, PlayerEntity player) {
        super(CraftingModule.CONTAINER_CRAFTING_CARD.get(), id, CONTAINER_FACTORY.get(), pos, null, player);
        cardIndex = player.inventory.selected;
    }

    @Override
    public void setupInventories(IItemHandler itemHandler, PlayerInventory inventory) {
        addInventory(ContainerFactory.CONTAINER_PLAYER, new InvWrapper(inventory));
        generateSlots(inventory.player);
    }

    @Override
    protected Slot createSlot(SlotFactory slotFactory, PlayerEntity playerEntity, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
        if (slotType == SlotType.SLOT_PLAYERHOTBAR && index == cardIndex) {
            return new BaseSlot(inventories.get(slotFactory.getInventoryName()), null, slotFactory.getIndex(), slotFactory.getX(), slotFactory.getY()) {
                @Override
                public boolean mayPickup(PlayerEntity player) {
                    // We don't want to take the stack from this slot.
                    return false;
                }
            };
        } else {
            return super.createSlot(slotFactory, playerEntity, inventory, index, x, y, slotType);
        }
    }

    public void setGridContents(PlayerEntity player, List<ItemStack> stacks) {
        ItemStack craftingCard = player.getItemInHand(Hand.MAIN_HAND);
        ItemStackList s = ItemStackList.create(INPUT_SLOTS + 1);
        int x = 0;
        int y = 0;
        for (int i = 0; i < stacks.size(); i++) {
            if (i == 0) {
                s.set(SLOT_OUT, stacks.get(i));
            } else {
                int slot = y * GRID_WIDTH + x;
                s.set(slot, stacks.get(i));
                x++;
                if (x >= 3) {
                    x = 0;
                    y++;
                }
            }
        }
        CraftingCardItem.putStacksInItem(craftingCard, s);
    }
}
