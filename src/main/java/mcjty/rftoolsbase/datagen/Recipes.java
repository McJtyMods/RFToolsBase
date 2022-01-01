package mcjty.rftoolsbase.datagen;

import mcjty.lib.datagen.BaseRecipeProvider;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.modules.various.VariousModule;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Recipes extends BaseRecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
        add('F', VariousModule.MACHINE_FRAME.get());
        add('A', VariousModule.MACHINE_BASE.get());
        add('s', VariousModule.DIMENSIONALSHARD.get());
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(VariousModule.DIMENSIONALSHARD.get())
                .pattern("deg")
                .pattern("irG")
                .pattern("qcL")
                .define('q', Items.QUARTZ)
                .define('r', Items.REDSTONE)
                .define('c', Items.PRISMARINE_SHARD)
                .define('d', Items.DIAMOND)
                .define('e', Items.EMERALD)
                .define('g', Items.GOLD_INGOT)
                .define('G', Items.GLOWSTONE_DUST)
                .define('i', Items.IRON_INGOT)
                .define('L', Items.GLASS)
                .unlockedBy("gold", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT,
                        Items.QUARTZ, Items.REDSTONE, Items.PRISMARINE_SHARD, Items.DIAMOND,
                        Items.EMERALD, Items.GLOWSTONE_DUST, Items.IRON_INGOT, Items.GLASS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(VariousModule.MACHINE_BASE.get())
                .pattern("ggg")
                .pattern("sss")
                .define('g', Items.GOLD_NUGGET)
                .define('s', Tags.Items.STONE)
                .unlockedBy("gold", has(Items.GOLD_NUGGET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(VariousModule.MACHINE_FRAME.get())
                .pattern("ili")
                .pattern("g g")
                .pattern("ili")
                .define('g', Items.GOLD_NUGGET)
                .define('i', Items.IRON_INGOT)
                .define('l', Tags.Items.DYES_BLUE)
                .unlockedBy("iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT, Items.GOLD_NUGGET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(MachineInfuserModule.MACHINE_INFUSER.get())
                .pattern("srs")
                .pattern("dMd")
                .pattern("srs")
                .define('r', Items.REDSTONE)
                .define('s', VariousModule.DIMENSIONALSHARD.get())
                .define('d', Items.DIAMOND)
                .define('M', VariousModule.MACHINE_FRAME.get())
                .unlockedBy("machine_frame", InventoryChangeTrigger.TriggerInstance.hasItems(VariousModule.MACHINE_FRAME.get(), VariousModule.DIMENSIONALSHARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(VariousModule.SMARTWRENCH.get())
                .pattern("  i")
                .pattern(" l ")
                .pattern("l  ")
                .define('i', Items.IRON_INGOT)
                .define('l', Tags.Items.DYES_BLUE)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(VariousModule.INFUSED_DIAMOND.get())
                .pattern("sss")
                .pattern("sds")
                .pattern("sss")
                .define('s', VariousModule.DIMENSIONALSHARD.get())
                .define('d', Items.DIAMOND)
                .unlockedBy("shard", has(VariousModule.DIMENSIONALSHARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(VariousModule.INFUSED_ENDERPEARL.get())
                .pattern("sss")
                .pattern("sds")
                .pattern("sss")
                .define('s', VariousModule.DIMENSIONALSHARD.get())
                .define('d', Items.ENDER_PEARL)
                .unlockedBy("shard", has(VariousModule.DIMENSIONALSHARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(CraftingModule.CRAFTING_CARD.get(), 8)
                .pattern("pc")
                .pattern("rp")
                .define('c', Items.CRAFTING_TABLE)
                .define('r', Items.REDSTONE)
                .define('p', Items.PAPER)
                .unlockedBy("crafter", has(Items.CRAFTING_TABLE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(FilterModule.FILTER_MODULE.get())
                .pattern(" h ")
                .pattern("rir")
                .pattern(" p ")
                .define('h', Items.HOPPER)
                .define('r', Items.REDSTONE)
                .define('p', Items.PAPER)
                .define('i', Items.IRON_INGOT)
                .unlockedBy("hopper", has(Items.HOPPER))
                .save(consumer);
        build(consumer, ShapedRecipeBuilder.shaped(InformationScreenModule.INFORMATION_SCREEN.get())
                        .define('-', Tags.Items.GLASS_PANES)
                        .unlockedBy("frame", InventoryChangeTrigger.TriggerInstance.hasItems(VariousModule.MACHINE_BASE.get(), Items.REDSTONE)),
                "---", "rAr");
        build(consumer, ShapedRecipeBuilder.shaped(TabletModule.TABLET.get())
                        .define('g', Tags.Items.NUGGETS_GOLD)
                        .define('Q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                        .unlockedBy("quartz", has(Blocks.QUARTZ_BLOCK)),
                "geg", "RQR", "gRg");
        build(consumer, ShapedRecipeBuilder.shaped(VariousModule.MANUAL.get())
                        .define('X', Items.BOOK)
                        .define('g', Tags.Items.NUGGETS_IRON)
                        .unlockedBy("book", has(Items.BOOK)),
                "Xg", "gg");
    }
}
