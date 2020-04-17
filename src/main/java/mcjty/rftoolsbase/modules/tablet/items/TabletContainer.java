package mcjty.rftoolsbase.modules.tablet.items;

import mcjty.lib.container.*;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.tablet.TabletSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TabletContainer extends GenericContainer {
	private int cardIndex;

	public static final int NUM_SLOTS = 6;

	public static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory(1) {
        @Override
        protected void setup() {
        	box(SlotDefinition.specific(s -> s.getItem() instanceof ITabletSupport), CONTAINER_CONTAINER, 0, 15, 13, NUM_SLOTS, 18+5, 1, 18);
            playerSlots(10, 106);
        }
    };

	public TabletContainer(int id, BlockPos pos, PlayerEntity player) {
		super(TabletSetup.CONTAINER_TABLET.get(), id, CONTAINER_FACTORY, pos, null);
		cardIndex = player.inventory.currentItem;
    }

	@Override
	public void setupInventories(IItemHandler itemHandler, PlayerInventory inventory) {
		addInventory(ContainerFactory.CONTAINER_CONTAINER, itemHandler);
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
