package mcjty.rftoolsbase.modules.infuser;

import java.util.function.Supplier;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RBlock;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.items.BaseBlockItem;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import mcjty.rftoolsbase.modules.infuser.data.InfuserData;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.setup.Config;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import mcjty.lib.setup.DeferredHolder;
import static mcjty.lib.setup.Registration.ITEM_INFUSABLE;
import static mcjty.rftoolsbase.setup.Registration.*;

public class MachineInfuserModule implements IModule {

    public static final RBlock<BaseBlock, BlockItem, MachineInfuserTileEntity> MACHINE_INFUSER = RBLOCKS.registerBlock("machine_infuser",
            MachineInfuserTileEntity.class,
            MachineInfuserTileEntity::createBlock,
            (block, itemProperties) -> new BaseBlockItem(block.get(), itemProperties.useBlockDescriptionPrefix()),
            MachineInfuserTileEntity::new);
    public static final Supplier<MenuType<GenericContainer>> CONTAINER_MACHINE_INFUSER = CONTAINERS.register("machine_infuser", GenericContainer::createContainerType);

    public static final AttachmentType<InfuserData> INFUSER_DATA = net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(
            net.minecraft.resources.Identifier.fromNamespaceAndPath("rftoolsbase", "infuser_data"), builder -> builder.initializer(() -> new InfuserData(0)).persistent(InfuserData.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<InfuserData>> ITEM_INFUSER_DATA = COMPONENTS.registerComponentType(
            "infuser_data",
            builder -> builder
                    .persistent(InfuserData.CODEC)
                    .networkSynchronized(InfuserData.STREAM_CODEC));

    @Override
    public void init() {

    }

    @Override
    public void initClient() {
        registerScreens();
    }

    public void registerScreens() {
        GuiMachineInfuser.register();
    }

    @Override
    public void initConfig() {
        MachineInfuserConfiguration.init(Config.SERVER_BUILDER);
    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.blockBuilder(MACHINE_INFUSER)
                        .ironPickaxeTags()
                        .standardLoot(ITEM_INFUSER_DATA.get(), ITEM_INFUSABLE.get())
                        .shaped(builder -> builder
                                .define('s', VariousModule.DIMENSIONALSHARD.get())
                                .define('M', VariousModule.MACHINE_FRAME.get())
                                .unlockedBy("machine_frame", InventoryChangeTrigger.TriggerInstance.hasItems(VariousModule.MACHINE_FRAME.get(), VariousModule.DIMENSIONALSHARD.get())),
                                "srs", "dMd", "srs")
        );
    }
}
