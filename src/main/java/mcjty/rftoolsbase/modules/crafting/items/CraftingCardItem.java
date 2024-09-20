package mcjty.rftoolsbase.modules.crafting.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.crafting.BaseRecipe;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;
import static mcjty.lib.builder.TooltipBuilder.parameter;
import static mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer.GRID_WIDTH;
import static mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer.INPUT_SLOTS;

public class CraftingCardItem extends Item implements ITooltipSettings {

    public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/craftingcard");
    private static final CraftingContainer CRAFTING_INVENTORY = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
        @Override
        public boolean stillValid(@Nonnull Player playerIn) {
            return false;
        }

        @Override
        public ItemStack quickMoveStack(Player player, int slot) {
            return ItemStack.EMPTY;
        }
    }, 3, 3);

    @Override
    public ManualEntry getManualEntry() {
        return MANUAL;
    }

    private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(() -> new TooltipBuilder()
            .info(header(),
                    parameter("info", stack -> {
                        ItemStack result = getResult(stack);
                        if (!result.isEmpty()) {
                            if (result.getCount() > 1) {
                                return result.getHoverName().getString() /* was getFormattedText() */ + "(" + result.getCount() + ")";
                            } else {
                                return result.getHoverName().getString() /* was getFormattedText() */;
                            }
                        }
                        return "<empty>";
                    })));

    public CraftingCardItem() {
        super(RFToolsBase.setup.defaultProperties()
                .durability(0)
                .stacksTo(1));
    }

    @Nullable
    private static Recipe<?> findRecipeInternal(Level world, CraftingInput inv, RecipeType<?> type) {
        for (RecipeHolder<?> rh : world.getRecipeManager().getRecipes()) {
            Recipe<?> r = rh.value();
            if (r != null && type.equals(r.getType()) && recipeMatch((Recipe<CraftingInput>)r, inv, world)) {
                return r;
            }
        }
        return null;
    }

    private static <T extends RecipeInput> boolean recipeMatch(Recipe<T> r, T inv, Level world) {
        return r.matches(inv, world);
    }

    /**
     * If the recipe in this card is valid then this will return the matching IRecipe
     */
    @Nullable
    public static Recipe findRecipe(Level world, ItemStack craftingCard, RecipeType<?> type) {
        ItemStackList stacks = getStacksFromItem(craftingCard);
        for (int y = 0 ; y < 3 ; y++) {
            for (int x = 0 ; x < 3 ; x++) {
                int idx = y*3+x;
                int idxCard = y*GRID_WIDTH + x;
                CRAFTING_INVENTORY.setItem(idx, stacks.get(idxCard));
            }
        }
        return findRecipeInternal(world, CRAFTING_INVENTORY, type);
    }

    public static void testRecipe(Level world, ItemStack craftingCard) {
        ItemStackList stacks = getStacksFromItem(craftingCard);
        for (int y = 0 ; y < 3 ; y++) {
            for (int x = 0 ; x < 3 ; x++) {
                int idx = y*3+x;
                int idxCard = y*GRID_WIDTH + x;
                CRAFTING_INVENTORY.setItem(idx, stacks.get(idxCard));
            }
        }
        Recipe recipe = findRecipeInternal(world, CRAFTING_INVENTORY, RecipeType.CRAFTING);
        if (recipe != null) {
            ItemStack stack = BaseRecipe.assemble(recipe, CRAFTING_INVENTORY, world);
            stacks.set(INPUT_SLOTS, stack);
        } else {
            stacks.set(INPUT_SLOTS, ItemStack.EMPTY);
        }
        putStacksInItem(craftingCard, stacks);
    }

    public static ItemStackList getStacksFromItem(ItemStack craftingCard) {
        CompoundTag tagCompound = craftingCard.getOrCreateTag();
        ItemStackList stacks = ItemStackList.create(INPUT_SLOTS+1);
        ListTag bufferTagList = tagCompound.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            CompoundTag nbtTagCompound = bufferTagList.getCompound(i);
            stacks.set(i, ItemStack.of(nbtTagCompound));
        }
        return stacks;
    }

    public static void putStacksInItem(ItemStack craftingCard, ItemStackList stacks) {
        CompoundTag tagCompound = craftingCard.getOrCreateTag();
        ListTag bufferTagList = new ListTag();
        for (ItemStack stack : stacks) {
            CompoundTag nbtTagCompound = new CompoundTag();
            if (!stack.isEmpty()) {
                stack.save(nbtTagCompound);
            }
            bufferTagList.add(nbtTagCompound);
        }
        tagCompound.put("Items", bufferTagList);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> list, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, list, flagIn);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), stack, list, flagIn);
        // @todo tooltip icons
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }
        if (!world.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
                @Override
                @Nonnull
                public Component getDisplayName() {
                    return ComponentFactory.literal("Crafting Card");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player player) {
                    CraftingCardContainer container = new CraftingCardContainer(id, player.blockPosition(), player);
                    container.setupInventories(null, playerInventory);
                    return container;
                }
            });
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public static ItemStack getResult(ItemStack card) {
        CompoundTag tagCompound = card.getTag();
        if (tagCompound == null) {
            return ItemStack.EMPTY;
        }
        ListTag bufferTagList = tagCompound.getList("Items", Tag.TAG_COMPOUND);
        CompoundTag nbtTagCompound = bufferTagList.getCompound(CraftingCardContainer.SLOT_OUT);
        return ItemStack.of(nbtTagCompound);
    }

    private static boolean isInGrid(int index) {
        int x = index % 5;
        int y = index / 5;
        return x <= 2 && y <= 2;
    }

    // Return true if this crafting card fits a 3x3 crafting grid nicely
    public static boolean fitsGrid(ItemStack card) {
        CompoundTag tagCompound = card.getTag();
        if (tagCompound == null) {
            return false;
        }
        ListTag bufferTagList = tagCompound.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundTag nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.of(nbtTagCompound);
                if (!s.isEmpty()) {
                    if (!isInGrid(i)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static List<Ingredient> getIngredientsGrid(ItemStack card) {
        CompoundTag tagCompound = card.getTag();
        if (tagCompound == null) {
            return Collections.emptyList();
        }
        ListTag bufferTagList = tagCompound.getList("Items", Tag.TAG_COMPOUND);
        List<Ingredient> stacks = new ArrayList<>();
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundTag nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.of(nbtTagCompound);
                if (isInGrid(i)) {
                    stacks.add(Ingredient.of(s));
                }
            }
        }
        return stacks;
    }

    public static List<ItemStack> getIngredientStacks(ItemStack card) {
        CompoundTag tagCompound = card.getTag();
        if (tagCompound == null) {
            return Collections.emptyList();
        }
        ListTag bufferTagList = tagCompound.getList("Items", Tag.TAG_COMPOUND);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundTag nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.of(nbtTagCompound);
                if (!s.isEmpty()) {
                    stacks.add(s);
                }
            }
        }
        return stacks;
    }

    /**
     * Get the stacks in this card as a list of Ingredient
     */
    public static List<Ingredient> getIngredients(ItemStack card) {
        CompoundTag tagCompound = card.getTag();
        if (tagCompound == null) {
            return Collections.emptyList();
        }
        ListTag bufferTagList = tagCompound.getList("Items", Tag.TAG_COMPOUND);
        List<Ingredient> stacks = new ArrayList<>();
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundTag nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.of(nbtTagCompound);
                if (!s.isEmpty()) {
                    stacks.add(Ingredient.of(s));
                }
            }
        }
        return stacks;
    }
}
