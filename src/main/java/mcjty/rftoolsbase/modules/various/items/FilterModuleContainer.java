package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.container.*;
import mcjty.rftoolsbase.modules.various.VariousSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FilterModuleContainer extends GenericContainer {
    public static final String CONTAINER_INVENTORY = "container";

	private int cardIndex;

	public static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory(0) {
        @Override
        protected void setup() {
            playerSlots(60, 106);
        }
    };

	public FilterModuleContainer(int id, BlockPos pos, PlayerEntity player) {
		super(VariousSetup.CONTAINER_FILTER_MODULE.get(), id, CONTAINER_FACTORY, pos, null);
		cardIndex = player.inventory.currentItem;
    }

	@Override
	public void setupInventories(IItemHandler itemHandler, PlayerInventory inventory) {
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
}
