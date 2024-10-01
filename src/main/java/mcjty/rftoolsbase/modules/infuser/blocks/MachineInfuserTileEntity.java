package mcjty.rftoolsbase.modules.infuser.blocks;

import mcjty.lib.api.container.DefaultContainerProvider;
import mcjty.lib.api.infusable.DefaultInfusable;
import mcjty.lib.api.infusable.IInfusable;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.container.GenericItemHandler;
import mcjty.lib.tileentity.Cap;
import mcjty.lib.tileentity.CapType;
import mcjty.lib.tileentity.GenericEnergyStorage;
import mcjty.lib.tileentity.TickingTileEntity;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserConfiguration;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.infuser.data.InfuserData;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.Nonnull;
import java.util.Optional;

import static mcjty.lib.api.container.DefaultContainerProvider.container;
import static mcjty.lib.builder.TooltipBuilder.*;
import static mcjty.lib.container.SlotDefinition.specific;

public class MachineInfuserTileEntity extends TickingTileEntity {

    public static final int SLOT_SHARDINPUT = 0;
    public static final int SLOT_MACHINEOUTPUT = 1;

    public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(2)
            .slot(specific(MachineInfuserTileEntity::isShard).in(), SLOT_SHARDINPUT, 64, 24)
            .slot(specific(MachineInfuserTileEntity::isInfusable).in().out(), SLOT_MACHINEOUTPUT, 118, 24)
            .playerSlots(10, 70));

    @Cap(type = CapType.ITEMS_AUTOMATION)
    private final GenericItemHandler items = GenericItemHandler.create(this, CONTAINER_FACTORY)
            .slotLimit(slot -> slot == SLOT_MACHINEOUTPUT ? 1 : 64)
            .insertable((slot, stack) -> {
                if (slot == SLOT_MACHINEOUTPUT) {
                    return isInfusable(stack);
                } else {
                    return isShard(stack);
                }
            })
            .build();

    @Cap(type = CapType.ENERGY)
    private final GenericEnergyStorage energyStorage = new GenericEnergyStorage(this, true, MachineInfuserConfiguration.MAXENERGY.get(), MachineInfuserConfiguration.RECEIVEPERTICK.get());

    @Cap(type = CapType.CONTAINER)
    private final Lazy<MenuProvider> screenHandler = Lazy.of(() -> new DefaultContainerProvider<GenericContainer>("Machine Infuser")
            .containerSupplier(container(MachineInfuserModule.CONTAINER_MACHINE_INFUSER, CONTAINER_FACTORY,this))
            .itemHandler(() -> items)
            .energyHandler(() -> energyStorage)
            .setupSync(this));

    @Cap(type = CapType.INFUSABLE)
    private final IInfusable infusableHandler = new DefaultInfusable(MachineInfuserTileEntity.this);

    public MachineInfuserTileEntity(BlockPos pos, BlockState state) {
        super(MachineInfuserModule.MACHINE_INFUSER.be().get(), pos, state);
    }

    public static BaseBlock createBlock() {

        return new BaseBlock(new BlockBuilder()
                .tileEntitySupplier(MachineInfuserTileEntity::new)
                .infusable()
                .manualEntry(ManualHelper.create("rftoolsbase:machines/infusing"))
                .info(key("message.rftoolsbase.shiftmessage"))
                .infoShift(header(), gold()));
    }

    @Override
    public void tickServer() {
        var data = getData(MachineInfuserModule.INFUSER_DATA);
        int infusing = data.infusing();
        if (infusing > 0) {
            infusing--;
            if (infusing == 0) {
                ItemStack outputStack = items.getStackInSlot(1);
                finishInfusing(outputStack);
            }
            setData(MachineInfuserModule.INFUSER_DATA, new InfuserData(infusing));
        } else {
            ItemStack inputStack = items.getStackInSlot(0);
            ItemStack outputStack = items.getStackInSlot(1);
            if (isShard(inputStack) && isInfusable(outputStack)) {
                startInfusing();
            }
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        var data = input.get(MachineInfuserModule.ITEM_INFUSER_DATA);
        if (data != null) {
            setData(MachineInfuserModule.INFUSER_DATA, data);
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        var data = getData(MachineInfuserModule.INFUSER_DATA);
        builder.set(MachineInfuserModule.ITEM_INFUSER_DATA, data);
    }

    private static boolean isShard(ItemStack stack) {
        return TagTools.hasTag(stack.getItem(), VariousModule.SHARDS_TAG);
    }

    private static boolean isInfusable(ItemStack stack) {
        return getStackIfInfusable(stack).map(s -> BaseBlock.getInfused(s) < MachineInfuserConfiguration.MAX_INFUSE.get()).orElse(false);
    }

    @Nonnull
    private static Optional<ItemStack> getStackIfInfusable(ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) {
            return Optional.empty();
        }
        Block block = ((BlockItem) item).getBlock();
        if (block instanceof BaseBlock && ((BaseBlock) block).isInfusable()) {
            return Optional.of(stack);
        } else {
            return Optional.empty();
        }
    }

    private void finishInfusing(ItemStack stack) {
        getStackIfInfusable(stack).ifPresent(s -> {
            BaseBlock.setInfused(s, BaseBlock.getInfused(s)+1);
        });
    }

    private void startInfusing() {
        int defaultCost = MachineInfuserConfiguration.RFPERTICK.get();
        int rf = (int) (defaultCost * (2.0f - infusableHandler.getInfusedFactor()) / 2.0f);

        if (energyStorage.getEnergy() < rf) {
            // Not enough energy.
            return;
        }
        energyStorage.consumeEnergy(rf);

        items.getStackInSlot(0).split(1);
        if (items.getStackInSlot(0).isEmpty()) {
            items.setStackInSlot(0, ItemStack.EMPTY);
        }
        setData(MachineInfuserModule.INFUSER_DATA, new InfuserData(5));
    }

//    @Override
//    protected void loadAdditional(CompoundTag tagCompound, HolderLookup.Provider pRegistries) {
//        super.loadAdditional(tagCompound, pRegistries);
//        infusing = tagCompound.getCompound("Info").getInt("infusing");
//    }
//
//    @Override
//    protected void saveAdditional(CompoundTag tagCompound, HolderLookup.Provider provider) {
//        super.saveAdditional(tagCompound, provider);
//        getOrCreateInfo(tagCompound).putInt("infusing", infusing);
//    }
}
