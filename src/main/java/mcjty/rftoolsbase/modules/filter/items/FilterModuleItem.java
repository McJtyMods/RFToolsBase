package mcjty.rftoolsbase.modules.filter.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.tooltips.ITooltipExtras;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.InventoryTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.FilterModuleCache;
import mcjty.rftoolsbase.tools.ManualHelper;
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
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
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
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mcjty.lib.builder.TooltipBuilder.*;

import net.minecraft.item.Item.Properties;

public class FilterModuleItem extends Item implements ITooltipSettings, ITooltipExtras {

    public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/filtermodule");
    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
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
                .stacksTo(1)
                .tab(RFToolsBase.setup.getTab()));
    }

    @Override
    public ManualEntry getManualEntry() {
        return MANUAL;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, list, flagIn);
        tooltipBuilder.get().makeTooltip(getRegistryName(), itemStack, list, flagIn);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World world = context.getLevel();
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = context.getClickedPos();
        if (player.isCrouching()) {
            if (!world.isClientSide) {
                TileEntity te = world.getBlockEntity(pos);
                if (InventoryTools.isInventory(te)) {
                    FilterModuleInventory inventory = new FilterModuleInventory(stack);
                    InventoryTools.getItems(te, s -> true).forEach(inventory::addStack);
                    inventory.markDirty();
                    player.displayClientMessage(new StringTextComponent(TextFormatting.GREEN + "Stored inventory contents in filter"), false);
                } else {
                    BlockState state = world.getBlockState(pos);
                    ItemStack blockStack = state.getBlock().getCloneItemStack(world, pos, state);
                    if (!blockStack.isEmpty()) {
                        FilterModuleInventory inventory = new FilterModuleInventory(stack);
                        inventory.addStack(blockStack);
                        inventory.markDirty();
                        player.displayClientMessage(new StringTextComponent(TextFormatting.GREEN + "Added " + blockStack.getHoverName().getString() /* was getFormattedText() */ + " to the filter!"), false);
                    } else {
                        player.displayClientMessage(new StringTextComponent(TextFormatting.RED + "Could not add " + blockStack.getHoverName().getString() /* was getFormattedText() */ + " to the filter!"), false);
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("Filter Module");
                }

                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    FilterModuleContainer container = new FilterModuleContainer(id, player.blockPosition(), player);
                    container.setupInventories(null, playerInventory);
                    return container;
                }
            });

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public static Predicate<ItemStack> getCache(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return new FilterModuleCache(stack);
    }

    @Override
    public List<Pair<ItemStack, Integer>> getItems(ItemStack stack) {
        FilterModuleInventory inventory = new FilterModuleInventory(stack);
        Set<Item> itemSet = new HashSet<>();
        for (ItemStack s : inventory.getStacks()) {
            itemSet.add(s.getItem());
        }
        for (ResourceLocation tag : inventory.getTags()) {
            ITag<Item> itemTag = ItemTags.getAllTags().getTag(tag);
            if (itemTag != null) {
                itemSet.addAll(itemTag.getValues());
            } else {
                ITag<Block> blockTag = BlockTags.getAllTags().getTag(tag);
                if (blockTag != null) {
                    for (Block block : blockTag.getValues()) {
                        itemSet.add(block.asItem());
                    }
                }
            }
        }
        return itemSet.stream().map(item -> Pair.of(new ItemStack(item), ITooltipExtras.NOAMOUNT)).collect(Collectors.toList());
    }
}
