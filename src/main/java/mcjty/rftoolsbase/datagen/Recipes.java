package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.items.ModItems;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ModItems.DIMENSIONALSHARD)
                .patternLine("deg")
                .patternLine("irG")
                .patternLine("qcL")
                .key('q', Items.QUARTZ)
                .key('r', Items.REDSTONE)
                .key('c', Items.PRISMARINE_SHARD)
                .key('d', Items.DIAMOND)
                .key('e', Items.EMERALD)
                .key('g', Items.GOLD_INGOT)
                .key('G', Items.GLOWSTONE_DUST)
                .key('i', Items.IRON_INGOT)
                .key('L', Items.GLASS)
                .setGroup("rftools")
                .addCriterion("gold", InventoryChangeTrigger.Instance.forItems(Items.GOLD_INGOT,
                        Items.QUARTZ, Items.REDSTONE, Items.PRISMARINE_SHARD, Items.DIAMOND,
                        Items.EMERALD, Items.GLOWSTONE_DUST, Items.IRON_INGOT, Items.GLASS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.MACHINE_BASE)
                .patternLine("ggg")
                .patternLine("sss")
                .key('g', Items.GOLD_NUGGET)
                .key('s', Tags.Items.STONE)
                .setGroup("rftools")
                .addCriterion("gold", InventoryChangeTrigger.Instance.forItems(Items.GOLD_NUGGET))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.MACHINE_FRAME)
                .patternLine("ili")
                .patternLine("g g")
                .patternLine("ili")
                .key('g', Items.GOLD_NUGGET)
                .key('i', Items.IRON_INGOT)
                .key('l', Tags.Items.DYES_BLUE)
                .setGroup("rftools")
                .addCriterion("iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT, Items.GOLD_NUGGET))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineInfuserSetup.MACHINE_INFUSER)
                .patternLine("srs")
                .patternLine("dMd")
                .patternLine("srs")
                .key('r', Items.REDSTONE)
                .key('s', ModItems.DIMENSIONALSHARD)
                .key('d', Items.DIAMOND)
                .key('M', ModItems.MACHINE_FRAME)
                .setGroup("rftools")
                .addCriterion("machine_frame", InventoryChangeTrigger.Instance.forItems(ModItems.MACHINE_FRAME, ModItems.DIMENSIONALSHARD))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.SMARTWRENCH)
                .patternLine("  i")
                .patternLine(" l ")
                .patternLine("l  ")
                .key('i', Items.IRON_INGOT)
                .key('l', Tags.Items.DYES_BLUE)
                .setGroup("rftools")
                .addCriterion("iron", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.INFUSED_DIAMOND)
                .patternLine("sss")
                .patternLine("sds")
                .patternLine("sss")
                .key('s', ModItems.DIMENSIONALSHARD)
                .key('d', Items.DIAMOND)
                .setGroup("rftools")
                .addCriterion("shard", InventoryChangeTrigger.Instance.forItems(ModItems.DIMENSIONALSHARD))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.INFUSED_ENDERPEARL)
                .patternLine("sss")
                .patternLine("sds")
                .patternLine("sss")
                .key('s', ModItems.DIMENSIONALSHARD)
                .key('d', Items.ENDER_PEARL)
                .setGroup("rftools")
                .addCriterion("shard", InventoryChangeTrigger.Instance.forItems(ModItems.DIMENSIONALSHARD))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CraftingSetup.CRAFTING_CARD, 8)
                .patternLine("pc")
                .patternLine("rp")
                .key('c', Items.CRAFTING_TABLE)
                .key('r', Items.REDSTONE)
                .key('p', Items.PAPER)
                .setGroup("rftools")
                .addCriterion("crafter", InventoryChangeTrigger.Instance.forItems(Items.CRAFTING_TABLE))
                .build(consumer);
    }
}
