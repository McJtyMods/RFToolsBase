package mcjty.rftoolsbase.modules.tablet.items;

import mcjty.rftoolsbase.api.various.ITabletSupport;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import static mcjty.rftoolsbase.modules.tablet.items.TabletContainer.NUM_SLOTS;

/** Tablet contents are written back only after a transfer commits or a menu slot changes. */
public class TabletItemHandler extends ItemStacksResourceHandler implements IndexModifier<ItemResource> {
    private final Player player;

    public TabletItemHandler(Player player) {
        super(NUM_SLOTS);
        this.player = player;
        ItemStack tablet = player.getItemInHand(InteractionHand.MAIN_HAND);
        for (int i = 0; i < NUM_SLOTS; i++) {
            stacks.set(i, TabletItem.getContainingItem(tablet, i).copy());
        }
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return resource.getItem() instanceof ITabletSupport;
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        return 1;
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        TabletItem.setContainingItem(player, InteractionHand.MAIN_HAND, index, stacks.get(index));
    }
}
