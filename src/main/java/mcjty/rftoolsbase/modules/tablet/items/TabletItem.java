package mcjty.rftoolsbase.modules.tablet.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.varia.ItemStackTools;
import mcjty.lib.varia.NBTTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.api.various.IItemCycler;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.tablet.TabletSetup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;
import static mcjty.rftoolsbase.modules.tablet.items.TabletContainer.NUM_SLOTS;

public class TabletItem extends Item implements IItemCycler {

    private final TooltipBuilder tooltipBuilder = new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(), gold());

    public TabletItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(RFToolsBase.setup.getTab()));
    }

    public static int getCurrentItem(ItemStack stack) {
        return ItemStackTools.getTag(stack).map(tag -> tag.getInt("Current")).orElse(0);
    }

    public static void setCurrentItem(ItemStack stack, int current) {
        stack.getOrCreateTag().putInt("Current", current);
    }

    @Override
    public void cycle(PlayerEntity player, ItemStack stack, boolean next) {
        int currentItem = getCurrentItem(stack);
        int tries = 4;
        while (tries > 0) {
            if (next) {
                currentItem = (currentItem + 1) % NUM_SLOTS;
            } else {
                currentItem = (currentItem + NUM_SLOTS - 1) % NUM_SLOTS;
            }
            ItemStack containingItem = getContainingItem(stack, currentItem);
            if (!containingItem.isEmpty()) {
                setCurrentItem(stack, currentItem);
                player.sendStatusMessage(new StringTextComponent("Switched item"), false);
                return;
            }
            tries--;
        }
    }

    public static ItemStack getContainingItem(ItemStack stack, int slot) {
        return ItemStackTools.getTag(stack).map(tag -> ItemStack.read(tag.getCompound("Item" + slot))).orElse(ItemStack.EMPTY);
    }

    public static void setContainingItem(PlayerEntity player, Hand hand, int slot, ItemStack containingItem) {
        ItemStack stack = player.getHeldItem(hand);
        CompoundNBT tag = stack.getOrCreateTag();
        if (containingItem.isEmpty()) {
            tag.remove("Item" + slot);
        } else {
            CompoundNBT compound = new CompoundNBT();
            containingItem.write(compound);
            tag.put("Item" + slot, compound);
        }

        ItemStack newTablet;
        if (containingItem.isEmpty()) {
            newTablet = new ItemStack(TabletSetup.TABLET.get());
        } else {
            newTablet = new ItemStack(((ITabletSupport)containingItem.getItem()).getInstalledTablet());
        }
        newTablet.setTag(stack.getTag());
        player.setHeldItem(hand, newTablet);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            if (player.isShiftKeyDown()) {
                openTabletGui(player);
            } else {
                ItemStack containingItem = getContainingItem(stack, getCurrentItem(stack));
                if (containingItem.isEmpty()) {
                    openTabletGui(player);
                } else {
                    if (containingItem.getItem() instanceof ITabletSupport) {
                        ((ITabletSupport) containingItem.getItem()).openGui(player, stack, containingItem);
                    }
                }
            }

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    private void openTabletGui(PlayerEntity player) {
        NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Tablet");
            }

            @Override
            public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                TabletContainer container = new TabletContainer(id, player.getPosition(), player);
                container.setupInventories(new TabletItemHandler(player), playerInventory);
                return container;
            }
        });
    }


    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();

        return ActionResultType.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        super.addInformation(itemStack, world, list, flags);
        tooltipBuilder.makeTooltip(getRegistryName(), itemStack, list, flags);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

}