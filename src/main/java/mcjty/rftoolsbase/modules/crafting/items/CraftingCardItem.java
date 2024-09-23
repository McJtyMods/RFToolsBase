package mcjty.rftoolsbase.modules.crafting.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.crafting.BaseRecipe;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.data.CraftingCardData;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;
import static mcjty.lib.builder.TooltipBuilder.parameter;
import static mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer.*;

public class CraftingCardItem extends Item implements ITooltipSettings {

    public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/craftingcard");
    private static final CraftingInput CRAFTING_INVENTORY = CraftingInput.of(3, 3, createList());

    private static List<ItemStack> createList() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0 ; i < 9 ; i++) {
            list.add(ItemStack.EMPTY);
        }
        return list;
    }

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
                CRAFTING_INVENTORY.items().set(idx, stacks.get(idxCard));
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
                CRAFTING_INVENTORY.items().set(idx, stacks.get(idxCard));
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
        CraftingCardData data = craftingCard.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, CraftingCardData.EMPTY);
        ItemStackList stacks = ItemStackList.create(INPUT_SLOTS+1);
        for (int i = 0 ; i < stacks.size() ; i++) {
            if (i < data.stacks().size()) {
                stacks.set(i, data.stacks().get(i));
            } else {
                stacks.set(i, ItemStack.EMPTY);
            }
        }
        return stacks;
    }

    public static void putStacksInItem(ItemStack craftingCard, ItemStackList stacks) {
        craftingCard.update(CraftingModule.ITEM_CRAFTINGCARD_DATA, CraftingCardData.EMPTY, data -> {
            for (int i = 0 ; i < data.stacks().size() ; i++) {
                if (i < stacks.size()) {
                    data.stacks().set(i, stacks.get(i));
                } else {
                    data.stacks().add(ItemStack.EMPTY);
                }
            }
            return data;
        });
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, list, flagIn);
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
            player.openMenu(new MenuProvider() {
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
        CraftingCardData data = card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, CraftingCardData.EMPTY);
        return data.stacks().get(SLOT_OUT);
    }

    private static boolean isInGrid(int index) {
        int x = index % 5;
        int y = index / 5;
        return x <= 2 && y <= 2;
    }

    // Return true if this crafting card fits a 3x3 crafting grid nicely
    public static boolean fitsGrid(ItemStack card) {
        CraftingCardData data = card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, CraftingCardData.EMPTY);
        for (int i = 0 ; i < data.stacks().size() ; i++) {
            if (i < INPUT_SLOTS) {
                if (!isInGrid(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Ingredient> getIngredientsGrid(ItemStack card) {
        CraftingCardData data = card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, CraftingCardData.EMPTY);
        List<Ingredient> stacks = new ArrayList<>();
        for (int i = 0 ; i < data.stacks().size() ; i++) {
            if (i < INPUT_SLOTS) {
                if (isInGrid(i)) {
                    stacks.add(Ingredient.of(data.stacks().get(i)));
                }
            }
        }
        return stacks;
    }

    public static List<ItemStack> getIngredientStacks(ItemStack card) {
        CraftingCardData data = card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, CraftingCardData.EMPTY);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0 ; i < data.stacks().size() ; i++) {
            if (i < INPUT_SLOTS) {
                if (isInGrid(i)) {
                    stacks.add(data.stacks().get(i));
                }
            }
        }
        return stacks;
    }

    /**
     * Get the stacks in this card as a list of Ingredient
     */
    public static List<Ingredient> getIngredients(ItemStack card) {
        CraftingCardData data = card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, CraftingCardData.EMPTY);
        List<Ingredient> stacks = new ArrayList<>();
        for (int i = 0 ; i < data.stacks().size() ; i++) {
            if (i < INPUT_SLOTS) {
                stacks.add(Ingredient.of(data.stacks().get(i)));
            }
        }
        return stacks;
    }
}
