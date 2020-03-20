package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.container.*;
import mcjty.rftoolsbase.modules.various.VariousSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FilterModuleContainer extends GenericContainer {
    public static final String CONTAINER_INVENTORY = "container";

    public static final int SLOT_FILTER = 0;
    public static final int FILTER_SLOTS = 6*5;

	private int cardIndex;

	public static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory(FILTER_SLOTS) {
        @Override
        protected void setup() {
            box(SlotDefinition.ghost(), CONTAINER_INVENTORY, SLOT_FILTER, 10, 9, 6, 5);
            playerSlots(10, 106);
        }
    };

	public FilterModuleContainer(int id, BlockPos pos, PlayerEntity player) {
		super(VariousSetup.CONTAINER_FILTER_MODULE.get(), id, CONTAINER_FACTORY, pos, null);
		cardIndex = player.inventory.currentItem;
    }

	@Override
	public void setupInventories(IItemHandler itemHandler, PlayerInventory inventory) {
		addInventory(CONTAINER_INVENTORY, itemHandler);
		addInventory(ContainerFactory.CONTAINER_PLAYER, new InvWrapper(inventory));
		generateSlots();
	}

	@Override
	protected Slot createSlot(SlotFactory slotFactory, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
		if (slotType == SlotType.SLOT_PLAYERHOTBAR && index == cardIndex) {
			return new BaseSlot(inventories.get(slotFactory.getInventoryName()), te, slotFactory.getIndex(), slotFactory.getX(), slotFactory.getY()) {
				@Override
				public boolean canTakeStack(PlayerEntity player) {
					// We don't want to take the stack from this slot.
					return false;
				}
			};
		} else {
			return super.createSlot(slotFactory, inventory, index, x, y, slotType);
		}
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack() && index >= FILTER_SLOTS && index < FILTER_SLOTS + 36) {
			ItemStack stack = slot.getStack().copy();
			stack.setCount(1);
			IItemHandler inv = inventories.get(CONTAINER_INVENTORY);
			for (int i = 0; i < inv.getSlots(); i++) {
				if (inv.getStackInSlot(i).isEmpty()) {
					if (inv instanceof IItemHandlerModifiable) {
						((IItemHandlerModifiable) inv).setStackInSlot(i, stack);
					} else {
						throw new RuntimeException("ItemHandler should be modifiable!");
					}
					break;
				}
			}
			slot.onSlotChanged();

		}

		return ItemStack.EMPTY;
	}
}
