package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.McJtyLib;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.api.storage.IModularStorage;
import mcjty.rftoolsbase.api.storage.IStorageModuleItem;
import mcjty.rftoolsbase.modules.various.FilterModuleCache;
import net.minecraft.block.BlockState;
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
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static mcjty.rftoolsbase.modules.various.items.FilterModuleContainer.FILTER_SLOTS;

public class FilterModuleItem extends Item {

    public FilterModuleItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(RFToolsBase.setup.getTab()));
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
        super.addInformation(itemStack, worldIn, list, flagIn);
        CompoundNBT tagCompound = itemStack.getTag();
        if (tagCompound != null) {
            String blackListMode = tagCompound.getString("blacklistMode");
            String modeLine = "Mode " + ("Black".equals(blackListMode) ? "blacklist" : "whitelist");
            if (tagCompound.getBoolean("oredictMode")) {
                modeLine += ", Oredict";
            }
            if (tagCompound.getBoolean("damageMode")) {
                modeLine += ", Damage";
            }
            if (tagCompound.getBoolean("nbtMode")) {
                modeLine += ", NBT";
            }
            if (tagCompound.getBoolean("modMode")) {
                modeLine += ", Mod";
            }
            list.add(new StringTextComponent(TextFormatting.BLUE + modeLine));
        }
        if (McJtyLib.proxy.isShiftKeyDown()) {
            list.add(new StringTextComponent(TextFormatting.WHITE + "This filter module is for the Modular Storage block,"));
            list.add(new StringTextComponent(TextFormatting.WHITE + "the Builder or the Area Scanner."));
            list.add(new StringTextComponent(TextFormatting.WHITE + "This module can make sure the block only accepts"));
            list.add(new StringTextComponent(TextFormatting.WHITE + "certain types of items"));
            list.add(new StringTextComponent(TextFormatting.YELLOW + "Sneak-right click on an inventory to"));
            list.add(new StringTextComponent(TextFormatting.YELLOW + "configure the filter based on contents"));
        } else {
            list.add(new StringTextComponent(TextFormatting.WHITE + RFToolsBase.SHIFT_MESSAGE));
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World world = context.getWorld();
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos = context.getPos();
        if (player.isCrouching()) {
            if (!world.isRemote) {
                TileEntity te = world.getTileEntity(pos);
                if (InventoryHelper.isInventory(te)) {
                    ItemStackList stacks = ItemStackList.create();
                    Set<ResourceLocation> registeredItems = new HashSet<>();
                    InventoryHelper.getItems(te, s -> true).forEach(s -> addItem(te, stacks, registeredItems, s));
                    FilterModuleInventory.convertItemsToNBT(stack.getOrCreateTag(), stacks);
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Stored inventory contents in filter"), false);
                } else {
                    BlockState state = world.getBlockState(pos);
                    ItemStack blockStack = state.getBlock().getItem(world, pos, state);
                    if (!blockStack.isEmpty()) {
                        if (!stack.hasTag()) {
                            stack.setTag(new CompoundNBT());
                        }
                        Set<ResourceLocation> registeredItems = new HashSet<>();
                        ItemStackList stacks = ItemStackList.create(FILTER_SLOTS);
                        ListNBT bufferTagList = stack.getTag().getList("Items", Constants.NBT.TAG_COMPOUND);
                        for (int i = 0; i < bufferTagList.size(); i++) {
                            CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
                            stacks.set(i, ItemStack.read(nbtTagCompound));
                        }
                        for (int i = 0; i < FILTER_SLOTS; i++) {
                            if (stacks.get(i).isEmpty()) {
                                stacks.set(i, blockStack);
                                player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Added " + blockStack.getDisplayName().getFormattedText() + " to the filter!"), false);
                                FilterModuleInventory.convertItemsToNBT(stack.getTag(), stacks);
                                break;
                            }
                        }
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }

    private void addItem(TileEntity te, List<ItemStack> stacks, Set<ResourceLocation> registeredItems, ItemStack s) {
        if (registeredItems.contains(s.getItem().getRegistryName())) {
            return;
        }
        if (te instanceof IModularStorage) {
            if (s.getItem() instanceof IStorageModuleItem || s.getItem() instanceof FilterModuleItem) {// @todo 1.15 || s.getItem() instanceof StorageTypeItem) {
                return;
            }
        }
        if (stacks.size() < FILTER_SLOTS) {
            ItemStack copy = s.copy();
            copy.setCount(1);
            stacks.add(copy);
            registeredItems.add(s.getItem().getRegistryName());
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("Filter Module");
                }

                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    FilterModuleContainer container = new FilterModuleContainer(id, player.getPosition(), player);
                    container.setupInventories(new FilterModuleInventory(player), playerInventory);
                    return container;
                }
            });

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public static FilterModuleCache getCache(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return new FilterModuleCache(stack);
    }
}
