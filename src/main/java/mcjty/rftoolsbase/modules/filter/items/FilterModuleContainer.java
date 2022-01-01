package mcjty.rftoolsbase.modules.filter.items;

import mcjty.lib.container.*;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FilterModuleContainer extends GenericContainer {

	private int cardIndex;

	public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(0)
			.playerSlots(60, 106));

	public FilterModuleContainer(int id, BlockPos pos, Player player) {
		super(FilterModule.CONTAINER_FILTER_MODULE.get(), id, CONTAINER_FACTORY.get(), pos, null);
		cardIndex = player.inventory.selected;
    }

	@Override
	public void setupInventories(IItemHandler itemHandler, Inventory inventory) {
		addInventory(ContainerFactory.CONTAINER_PLAYER, new InvWrapper(inventory));
		generateSlots(inventory.player);
	}

	@Override
	protected Slot createSlot(SlotFactory slotFactory, Player playerEntity, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
		if (slotType == SlotType.SLOT_PLAYERHOTBAR && index == cardIndex) {
			return new BaseSlot(inventories.get(slotFactory.getInventoryName()), te, slotFactory.getIndex(), slotFactory.getX(), slotFactory.getY()) {
				@Override
				public boolean mayPickup(Player player) {
					// We don't want to take the stack from this slot.
					return false;
				}
			};
		} else {
			return super.createSlot(slotFactory, playerEntity, inventory, index, x, y, slotType);
		}
	}
}
