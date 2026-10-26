package mcjty.rftoolsbase.setup;


import mcjty.lib.blocks.RBlockRegistry;
import mcjty.lib.setup.DeferredBlocks;
import mcjty.lib.setup.DeferredItems;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.VariousModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import mcjty.lib.setup.DeferredRegister;

import java.util.function.Supplier;

import static mcjty.rftoolsbase.RFToolsBase.MODID;

public class Registration {

    public static final RBlockRegistry RBLOCKS = new RBlockRegistry(MODID, RFToolsBase.setup::addTabItem);
    public static final DeferredBlocks BLOCKS = DeferredBlocks.create(MODID);
    public static final DeferredItems ITEMS = DeferredItems.create(MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<MapCodec<? extends PlacementModifier>> PLACEMENT_MODIFIERS = Tools.createPlacementRegistry(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static void register() {
        RBLOCKS.register();
        BLOCKS.register();
        ITEMS.register();
        TILES.register();
        CONTAINERS.register();
        SOUNDS.register();
        ENTITIES.register();
        PLACEMENT_MODIFIERS.register();
        TABS.register();
        COMPONENTS.register();
    }


    public static Item.Properties createStandardProperties() {
        return RFToolsBase.setup.defaultProperties();
    }

    public static Supplier<CreativeModeTab> TAB = TABS.register(MODID, () -> net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID))
            .icon(() -> new ItemStack(VariousModule.SMARTWRENCH.get()))
            .displayItems((featureFlags, output) -> {
                RFToolsBase.setup.populateTab(output);
            })
            .build());
}
