package mcjty.rftoolsbase.modules.informationscreen;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenBlock;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.modules.informationscreen.client.InformationScreenRenderer;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import mcjty.lib.setup.DeferredBlock;
import mcjty.lib.setup.DeferredItem;

import java.util.function.Supplier;

import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.*;

public class InformationScreenModule implements IModule {

    public static final DeferredBlock<Block> INFORMATION_SCREEN = BLOCKS.register("information_screen", InformationScreenBlock::new);
    public static final DeferredItem<Item> INFORMATION_SCREEN_ITEM = ITEMS.register("information_screen", tab(properties -> new BlockItem(INFORMATION_SCREEN.get(), properties.useBlockDescriptionPrefix())));
    public static final Supplier<BlockEntityType<InformationScreenTileEntity>> TYPE_INFORMATION_SCREEN = TILES.register("information_screen", () -> new BlockEntityType<>(InformationScreenTileEntity::new, java.util.Set.of(INFORMATION_SCREEN.get())));

    @Override
    public void init() {
    }

    @Override
    public void initClient() {
        InformationScreenRenderer.register();
    }

    @Override
    public void initConfig() {

    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.blockBuilder(INFORMATION_SCREEN)
                        .simpleLoot()
                        .ironPickaxeTags()
                        .shaped(builder -> builder
                                        .define('-', net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.parse("c:glass_panes")))
                                        .define('A', VariousModule.MACHINE_BASE.get())
                                        .unlockedBy("frame", InventoryChangeTrigger.TriggerInstance.hasItems(VariousModule.MACHINE_BASE.get(), Items.REDSTONE)),
                                "---", "rAr")
        );
    }
}
