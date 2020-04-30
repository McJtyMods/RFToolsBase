package mcjty.rftoolsbase.datagen;

import mcjty.lib.datagen.BaseRecipeProvider;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.filter.FilterSetup;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenSetup;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.tablet.TabletSetup;
import mcjty.rftoolsbase.modules.various.VariousSetup;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends BaseRecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
        add('F', VariousSetup.MACHINE_FRAME.get());
        add('A', VariousSetup.MACHINE_BASE.get());
        add('s', VariousSetup.DIMENSIONALSHARD.get());
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(VariousSetup.DIMENSIONALSHARD.get())
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
                .addCriterion("gold", InventoryChangeTrigger.Instance.forItems(Items.GOLD_INGOT,
                        Items.QUARTZ, Items.REDSTONE, Items.PRISMARINE_SHARD, Items.DIAMOND,
                        Items.EMERALD, Items.GLOWSTONE_DUST, Items.IRON_INGOT, Items.GLASS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VariousSetup.MACHINE_BASE.get())
                .patternLine("ggg")
                .patternLine("sss")
                .key('g', Items.GOLD_NUGGET)
                .key('s', Tags.Items.STONE)
                .addCriterion("gold", hasItem(Items.GOLD_NUGGET))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VariousSetup.MACHINE_FRAME.get())
                .patternLine("ili")
                .patternLine("g g")
                .patternLine("ili")
                .key('g', Items.GOLD_NUGGET)
                .key('i', Items.IRON_INGOT)
                .key('l', Tags.Items.DYES_BLUE)
                .addCriterion("iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT, Items.GOLD_NUGGET))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineInfuserSetup.MACHINE_INFUSER.get())
                .patternLine("srs")
                .patternLine("dMd")
                .patternLine("srs")
                .key('r', Items.REDSTONE)
                .key('s', VariousSetup.DIMENSIONALSHARD.get())
                .key('d', Items.DIAMOND)
                .key('M', VariousSetup.MACHINE_FRAME.get())
                .addCriterion("machine_frame", InventoryChangeTrigger.Instance.forItems(VariousSetup.MACHINE_FRAME.get(), VariousSetup.DIMENSIONALSHARD.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VariousSetup.SMARTWRENCH.get())
                .patternLine("  i")
                .patternLine(" l ")
                .patternLine("l  ")
                .key('i', Items.IRON_INGOT)
                .key('l', Tags.Items.DYES_BLUE)
                .addCriterion("iron", hasItem(Items.IRON_INGOT))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VariousSetup.INFUSED_DIAMOND.get())
                .patternLine("sss")
                .patternLine("sds")
                .patternLine("sss")
                .key('s', VariousSetup.DIMENSIONALSHARD.get())
                .key('d', Items.DIAMOND)
                .addCriterion("shard", hasItem(VariousSetup.DIMENSIONALSHARD.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VariousSetup.INFUSED_ENDERPEARL.get())
                .patternLine("sss")
                .patternLine("sds")
                .patternLine("sss")
                .key('s', VariousSetup.DIMENSIONALSHARD.get())
                .key('d', Items.ENDER_PEARL)
                .addCriterion("shard", hasItem(VariousSetup.DIMENSIONALSHARD.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CraftingSetup.CRAFTING_CARD.get(), 8)
                .patternLine("pc")
                .patternLine("rp")
                .key('c', Items.CRAFTING_TABLE)
                .key('r', Items.REDSTONE)
                .key('p', Items.PAPER)
                .addCriterion("crafter", hasItem(Items.CRAFTING_TABLE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(FilterSetup.FILTER_MODULE.get())
                .patternLine(" h ")
                .patternLine("rir")
                .patternLine(" p ")
                .key('h', Items.HOPPER)
                .key('r', Items.REDSTONE)
                .key('p', Items.PAPER)
                .key('i', Items.IRON_INGOT)
                .addCriterion("hopper", hasItem(Items.HOPPER))
                .build(consumer);
        build(consumer, ShapedRecipeBuilder.shapedRecipe(InformationScreenSetup.INFORMATION_SCREEN.get())
                        .key('-', Tags.Items.GLASS_PANES)
                        .addCriterion("frame", InventoryChangeTrigger.Instance.forItems(VariousSetup.MACHINE_BASE.get(), Items.REDSTONE)),
                "---", "rAr");
        build(consumer, ShapedRecipeBuilder.shapedRecipe(TabletSetup.TABLET.get())
                        .key('g', Tags.Items.NUGGETS_GOLD)
                        .key('Q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                        .addCriterion("quartz", hasItem(Blocks.QUARTZ_BLOCK)),
                "geg", "RQR", "gRg");
        build(consumer, ShapedRecipeBuilder.shapedRecipe(VariousSetup.MANUAL.get())
                        .key('X', Items.BOOK)
                        .key('g', Tags.Items.NUGGETS_IRON)
                        .addCriterion("book", hasItem(Items.BOOK)),
                "Xg", "gg");
    }
}
