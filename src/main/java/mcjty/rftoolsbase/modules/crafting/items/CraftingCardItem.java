package mcjty.rftoolsbase.modules.crafting.items;

import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer.GRID_WIDTH;
import static mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer.INPUT_SLOTS;

public class CraftingCardItem extends Item {

    private static final CraftingInventory CRAFTING_INVENTORY = new CraftingInventory(new Container(null, -1) {
        @Override
        public boolean canInteractWith(PlayerEntity playerIn) {
            return false;
        }
    }, 3, 3);

    public CraftingCardItem() {
        super(new Properties()
                .group(RFToolsBase.setup.getTab())
                .defaultMaxDamage(0)
                .maxStackSize(1));
    }

    @Nullable
    private static IRecipe findRecipeInternal(World world, CraftingInventory inv, IRecipeType type) {
        for (IRecipe r : world.getRecipeManager().getRecipes()) {
            if (r != null && type.equals(r.getType()) && r.matches(inv, world)) {
                return r;
            }
        }
        return null;
    }

    /**
     * If the recipe in this card is valid then this will return the matching IRecipe
     */
    @Nullable
    public static IRecipe findRecipe(World world, ItemStack craftingCard, IRecipeType type) {
        ItemStackList stacks = getStacksFromItem(craftingCard);
        for (int y = 0 ; y < 3 ; y++) {
            for (int x = 0 ; x < 3 ; x++) {
                int idx = y*3+x;
                int idxCard = y*GRID_WIDTH + x;
                CRAFTING_INVENTORY.setInventorySlotContents(idx, stacks.get(idxCard));
            }
        }
        return findRecipeInternal(world, CRAFTING_INVENTORY, type);
    }

    public static void testRecipe(World world, ItemStack craftingCard) {
        ItemStackList stacks = getStacksFromItem(craftingCard);
        for (int y = 0 ; y < 3 ; y++) {
            for (int x = 0 ; x < 3 ; x++) {
                int idx = y*3+x;
                int idxCard = y*GRID_WIDTH + x;
                CRAFTING_INVENTORY.setInventorySlotContents(idx, stacks.get(idxCard));
            }
        }
        IRecipe recipe = findRecipeInternal(world, CRAFTING_INVENTORY, IRecipeType.CRAFTING);
        if (recipe != null) {
            ItemStack stack = recipe.getCraftingResult(CRAFTING_INVENTORY);
            stacks.set(INPUT_SLOTS, stack);
        } else {
            stacks.set(INPUT_SLOTS, ItemStack.EMPTY);
        }
        putStacksInItem(craftingCard, stacks);
    }

    public static ItemStackList getStacksFromItem(ItemStack craftingCard) {
        CompoundNBT tagCompound = craftingCard.getOrCreateTag();
        ItemStackList stacks = ItemStackList.create(INPUT_SLOTS+1);
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
            stacks.set(i, ItemStack.read(nbtTagCompound));
        }
        return stacks;
    }

    public static void putStacksInItem(ItemStack craftingCard, ItemStackList stacks) {
        CompoundNBT tagCompound = craftingCard.getOrCreateTag();
        ListNBT bufferTagList = new ListNBT();
        for (ItemStack stack : stacks) {
            CompoundNBT nbtTagCompound = new CompoundNBT();
            if (!stack.isEmpty()) {
                stack.write(nbtTagCompound);
            }
            bufferTagList.add(nbtTagCompound);
        }
        tagCompound.put("Items", bufferTagList);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, list, flagIn);
        list.add(new StringTextComponent("This item can be used for auto"));
        list.add(new StringTextComponent("crafting. It stores ingredients"));
        list.add(new StringTextComponent("and end result for a recipe"));
        boolean strictnbt = CraftingCardItem.isStrictNBT(stack);
        list.add(new StringTextComponent(TextFormatting.GREEN + "Strict NBT: " + TextFormatting.WHITE + (strictnbt ? "yes" : "no")));
        ItemStack result = getResult(stack);
        if (!result.isEmpty()) {
            if (result.getCount() > 1) {
                list.add(new StringTextComponent(TextFormatting.BLUE + "Item: " + TextFormatting.WHITE + result.getDisplayName() + "(" +
                        result.getCount() + ")"));
            } else {
                list.add(new StringTextComponent(TextFormatting.BLUE + "Item: " + TextFormatting.WHITE + result.getDisplayName()));
            }
        }
        // @todo tooltip icons
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (hand != Hand.MAIN_HAND) {
            return new ActionResult<>(ActionResultType.PASS, stack);
        }
        if (!world.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("Crafting Card");
                }

                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    CraftingCardContainer container = new CraftingCardContainer(id, player.getPosition(), player);
                    container.setupInventories(null, playerInventory);
                    return container;
                }
            });
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public static ItemStack getResult(ItemStack card) {
        CompoundNBT tagCompound = card.getTag();
        if (tagCompound == null) {
            return ItemStack.EMPTY;
        }
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        CompoundNBT nbtTagCompound = bufferTagList.getCompound(CraftingCardContainer.SLOT_OUT);
        return ItemStack.read(nbtTagCompound);
    }

    private static boolean isInGrid(int index) {
        int x = index % 5;
        int y = index / 5;
        return x <= 2 && y <= 2;
    }

    // Return true if this crafting card fits a 3x3 crafting grid nicely
    public static boolean fitsGrid(ItemStack card) {
        CompoundNBT tagCompound = card.getTag();
        if (tagCompound == null) {
            return false;
        }
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.read(nbtTagCompound);
                if (!s.isEmpty()) {
                    if (!isInGrid(i)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isStrictNBT(ItemStack card) {
        CompoundNBT tagCompound = card.getTag();
        if (tagCompound == null) {
            return false;
        }
        return tagCompound.getBoolean("strictnbt");
    }

    public static List<Ingredient> getIngredientsGrid(ItemStack card) {
        CompoundNBT tagCompound = card.getTag();
        if (tagCompound == null) {
            return Collections.emptyList();
        }
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        List<Ingredient> stacks = new ArrayList<>();
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.read(nbtTagCompound);
                if (isInGrid(i)) {
                    stacks.add(Ingredient.fromStacks(s));
                }
            }
        }
        return stacks;
    }

    public static List<ItemStack> getIngredientStacks(ItemStack card) {
        CompoundNBT tagCompound = card.getTag();
        if (tagCompound == null) {
            return Collections.emptyList();
        }
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.read(nbtTagCompound);
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
        CompoundNBT tagCompound = card.getTag();
        if (tagCompound == null) {
            return Collections.emptyList();
        }
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        List<Ingredient> stacks = new ArrayList<>();
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            if (i < INPUT_SLOTS) {
                CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.read(nbtTagCompound);
                if (!s.isEmpty()) {
                    stacks.add(Ingredient.fromStacks(s));
                }
            }
        }
        return stacks;
    }
}
