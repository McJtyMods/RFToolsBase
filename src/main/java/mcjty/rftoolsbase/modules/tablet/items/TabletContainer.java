package mcjty.rftoolsbase.modules.tablet.items;

import mcjty.lib.container.*;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import static mcjty.lib.container.ContainerFactory.CONTAINER_CONTAINER;
import static mcjty.lib.container.SlotDefinition.specific;

public class TabletContainer extends GenericContainer {
	private final int cardIndex;

	public static final int NUM_SLOTS = 6;

	public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(1)
			.box(specific(s -> s.getItem() instanceof ITabletSupport), 0, 15, 13, NUM_SLOTS, 18+5, 1, 18)
            .playerSlots(10, 106));

	public TabletContainer(int id, BlockPos pos, Player player) {
		super(TabletModule.CONTAINER_TABLET.get(), id, CONTAINER_FACTORY.get(), pos, null, player);
		cardIndex = player.getInventory().selected;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().selected == cardIndex
                && player.getMainHandItem().getItem() instanceof TabletItem;
    }

    @Override
    public boolean clickMenuButton(Player player, int slot) {
        if (slot < 0 || slot >= NUM_SLOTS || !stillValid(player)) {
            return false;
        }
        TabletItem.setCurrentSlot(player, player.getMainHandItem(), slot);
        return true;
    }

	@Override
	public void setupInventories(IItemHandler itemHandler, Inventory inventory) {
		addInventory(CONTAINER_CONTAINER, itemHandler);
		addInventory(ContainerFactory.CONTAINER_PLAYER, new InvWrapper(inventory));
		generateSlots(inventory.player);
	}

	@Override
	protected Slot createSlot(SlotFactory slotFactory, Player playerEntity, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
		if (slotType == SlotType.SLOT_PLAYERHOTBAR && index == cardIndex) {
			return new BaseSlot(inventories.get(slotFactory.inventoryName()), te, slotFactory.index(), slotFactory.x(), slotFactory.y()) {
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
