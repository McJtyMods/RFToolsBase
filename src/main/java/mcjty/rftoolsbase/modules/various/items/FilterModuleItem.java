package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.tooltips.ITooltipExtras;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.FilterModuleCache;
import net.minecraft.block.Block;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
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
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;

public class FilterModuleItem extends Item implements ITooltipSettings, ITooltipExtras {

    private final TooltipBuilder tooltipBuilder = new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(), gold(),
                    parameter("info", stack -> {
                        CompoundNBT tagCompound = stack.getTag();
                        if (tagCompound != null) {
                            String blackListMode = tagCompound.getString("blacklistMode");
                            String modeLine = "Mode " + ("Black".equals(blackListMode) ? "blacklist" : "whitelist");
                            if (tagCompound.getBoolean("damageMode")) {
                                modeLine += ", Damage";
                            }
                            if (tagCompound.getBoolean("nbtMode")) {
                                modeLine += ", NBT";
                            }
                            if (tagCompound.getBoolean("modMode")) {
                                modeLine += ", Mod";
                            }
                            return modeLine;
                        }
                        return "<not configured>";
                    }));


    public FilterModuleItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(RFToolsBase.setup.getTab()));
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
        super.addInformation(itemStack, worldIn, list, flagIn);
        tooltipBuilder.makeTooltip(getRegistryName(), itemStack, list, flagIn);
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
                    FilterModuleInventory inventory = new FilterModuleInventory(stack);
                    InventoryHelper.getItems(te, s -> true).forEach(inventory::addStack);
                    inventory.markDirty();
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Stored inventory contents in filter"), false);
                } else {
                    BlockState state = world.getBlockState(pos);
                    ItemStack blockStack = state.getBlock().getItem(world, pos, state);
                    if (!blockStack.isEmpty()) {
                        FilterModuleInventory inventory = new FilterModuleInventory(stack);
                        inventory.addStack(blockStack);
                        inventory.markDirty();
                        player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Added " + blockStack.getDisplayName().getFormattedText() + " to the filter!"), false);
                    } else {
                        player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Could not add " + blockStack.getDisplayName().getFormattedText() + " to the filter!"), false);
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
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
                    container.setupInventories(null, playerInventory);
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

    @Override
    public List<Pair<ItemStack, Integer>> getItems(ItemStack stack) {
        FilterModuleInventory inventory = new FilterModuleInventory(stack);
        List<Pair<ItemStack, Integer>> list = new ArrayList<>();
        for (ItemStack s : inventory.getStacks()) {
            list.add(Pair.of(s, -2));
        }
        for (ResourceLocation tag : inventory.getTags()) {
            Tag<Item> itemTag = ItemTags.getCollection().get(tag);
            if (itemTag != null) {
                for (Item item : itemTag.getAllElements()) {
                    list.add(Pair.of(new ItemStack(item), -2));
                }
            } else {
                Tag<Block> blockTag = BlockTags.getCollection().get(tag);
                if (blockTag != null) {
                    for (Block block : blockTag.getAllElements()) {
                        list.add(Pair.of(new ItemStack(block), -2));
                    }
                }
            }
        }
        return list;
    }
}
