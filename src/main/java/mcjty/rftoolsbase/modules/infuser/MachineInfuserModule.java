package mcjty.rftoolsbase.modules.infuser;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import mcjty.rftoolsbase.modules.infuser.data.InfuserData;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Supplier;

import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.*;

public class MachineInfuserModule implements IModule {

    public static final DeferredBlock<BaseBlock> MACHINE_INFUSER = BLOCKS.register("machine_infuser", MachineInfuserTileEntity::createBlock);
    public static final DeferredItem<Item> MACHINE_INFUSER_ITEM = ITEMS.register("machine_infuser", tab(() -> new BlockItem(MACHINE_INFUSER.get(), Registration.createStandardProperties())));
    public static final Supplier<BlockEntityType<?>> TYPE_MACHINE_INFUSER = TILES.register("machine_infuser", () -> BlockEntityType.Builder.of(MachineInfuserTileEntity::new, MACHINE_INFUSER.get()).build(null));
    public static final Supplier<MenuType<GenericContainer>> CONTAINER_MACHINE_INFUSER = CONTAINERS.register("machine_infuser", GenericContainer::createContainerType);

    public static final Supplier<AttachmentType<InfuserData>> INFUSER_DATA = ATTACHMENT_TYPES.register(
            "infuser_data", () -> AttachmentType.builder(() -> new InfuserData(0))
                    .serialize(InfuserData.CODEC)
                    .build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<InfuserData>> ITEM_INFUSER_DATA = COMPONENTS.registerComponentType(
            "infuser_data",
            builder -> builder
                    .persistent(InfuserData.CODEC)
                    .networkSynchronized(InfuserData.STREAM_CODEC));

    public MachineInfuserModule(IEventBus bus) {
        bus.addListener(MachineInfuserModule::register);
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
    }

    public static void register(RegisterMenuScreensEvent event) {
        GuiMachineInfuser.register(event);
    }

    @Override
    public void initConfig(IEventBus bus) {
        MachineInfuserConfiguration.init(Config.SERVER_BUILDER);
    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.blockBuilder(MACHINE_INFUSER)
                        .ironPickaxeTags()
                        .standardLoot()
                        .shaped(builder -> builder
                                .define('s', VariousModule.DIMENSIONALSHARD.get())
                                .define('M', VariousModule.MACHINE_FRAME.get())
                                .unlockedBy("machine_frame", InventoryChangeTrigger.TriggerInstance.hasItems(VariousModule.MACHINE_FRAME.get(), VariousModule.DIMENSIONALSHARD.get())),
                                "srs", "dMd", "srs")
        );
    }
}
