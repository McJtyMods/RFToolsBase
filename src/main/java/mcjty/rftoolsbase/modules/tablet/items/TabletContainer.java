package mcjty.rftoolsbase.modules.tablet.items;

import mcjty.lib.container.*;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import static mcjty.lib.container.ContainerFactory.CONTAINER_CONTAINER;
import static mcjty.lib.container.SlotDefinition.specific;

public class TabletContainer extends GenericContainer {
	private int cardIndex;

	public static final int NUM_SLOTS = 6;

	public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(1)
			.box(specific(s -> s.getItem() instanceof ITabletSupport), CONTAINER_CONTAINER, 0, 15, 13, NUM_SLOTS, 18+5, 1, 18)
            .playerSlots(10, 106));

	public TabletContainer(int id, BlockPos pos, PlayerEntity player) {
		super(TabletModule.CONTAINER_TABLET.get(), id, CONTAINER_FACTORY.get(), pos, null);
		cardIndex = player.inventory.currentItem;
    }

	@Override
	public void setupInventories(IItemHandler itemHandler, PlayerInventory inventory) {
		addInventory(CONTAINER_CONTAINER, itemHandler);
		addInventory(ContainerFactory.CONTAINER_PLAYER, new InvWrapper(inventory));
		generateSlots(inventory.player);
	}

	@Override
	protected Slot createSlot(SlotFactory slotFactory, PlayerEntity playerEntity, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
		if (slotType == SlotType.SLOT_PLAYERHOTBAR && index == cardIndex) {
			return new BaseSlot(inventories.get(slotFactory.getInventoryName()), te, slotFactory.getIndex(), slotFactory.getX(), slotFactory.getY()) {
				@Override
				public boolean canTakeStack(PlayerEntity player) {
					// We don't want to take the stack from this slot.
					return false;
				}
			};
		} else {
			return super.createSlot(slotFactory, playerEntity, inventory, index, x, y, slotType);
		}
	}
}
