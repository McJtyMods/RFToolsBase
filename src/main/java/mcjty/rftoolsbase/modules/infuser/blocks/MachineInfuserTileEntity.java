package mcjty.rftoolsbase.modules.infuser.blocks;

import mcjty.lib.api.container.CapabilityContainerProvider;
import mcjty.lib.api.container.DefaultContainerProvider;
import mcjty.lib.api.infusable.CapabilityInfusable;
import mcjty.lib.api.infusable.DefaultInfusable;
import mcjty.lib.api.infusable.IInfusable;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.container.AutomationFilterItemHander;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.container.NoDirectionItemHander;
import mcjty.lib.tileentity.GenericEnergyStorage;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserConfiguration;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static mcjty.lib.builder.TooltipBuilder.*;
import static mcjty.lib.container.ContainerFactory.CONTAINER_CONTAINER;
import static mcjty.lib.container.SlotDefinition.specific;

public class MachineInfuserTileEntity extends GenericTileEntity implements ITickableTileEntity {

    public static final int SLOT_SHARDINPUT = 0;
    public static final int SLOT_MACHINEOUTPUT = 1;

    public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(2)
            .slot(specific(new ItemStack(VariousModule.DIMENSIONALSHARD.get())).in(), CONTAINER_CONTAINER, SLOT_SHARDINPUT, 64, 24)
            .slot(specific(MachineInfuserTileEntity::isInfusable).in().out(), CONTAINER_CONTAINER, SLOT_MACHINEOUTPUT, 118, 24)
            .playerSlots(10, 70));

    private final NoDirectionItemHander items = new NoDirectionItemHander(this, CONTAINER_FACTORY.get());
    private final LazyOptional<AutomationFilterItemHander> itemHandler = LazyOptional.of(() -> new AutomationFilterItemHander(items));

    private final GenericEnergyStorage energyStorage = new GenericEnergyStorage(this, true, MachineInfuserConfiguration.MAXENERGY.get(), MachineInfuserConfiguration.RECEIVEPERTICK.get());
    private final LazyOptional<GenericEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);
    private final LazyOptional<INamedContainerProvider> screenHandler = LazyOptional.of(() -> new DefaultContainerProvider<GenericContainer>("Machine Infuser")
            .containerSupplier((windowId,player) -> new GenericContainer(MachineInfuserModule.CONTAINER_MACHINE_INFUSER.get(), windowId, CONTAINER_FACTORY.get(), getPos(), MachineInfuserTileEntity.this))
            .itemHandler(() -> items)
            .energyHandler(() -> energyStorage));
    private final LazyOptional<IInfusable> infusableHandler = LazyOptional.of(() -> new DefaultInfusable(MachineInfuserTileEntity.this));

    private int infusing = 0;

    public MachineInfuserTileEntity() {
        super(MachineInfuserModule.TYPE_MACHINE_INFUSER.get());
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
    public void tick() {
        if (!world.isRemote) {
            tickServer();
        }
    }

    private void tickServer() {
        if (infusing > 0) {
            infusing--;
            if (infusing == 0) {
                ItemStack outputStack = items.getStackInSlot(1);
                finishInfusing(outputStack);
            }
            markDirtyQuick();
        } else {
            ItemStack inputStack = items.getStackInSlot(0);
            ItemStack outputStack = items.getStackInSlot(1);
            if (!inputStack.isEmpty() && inputStack.getItem() == VariousModule.DIMENSIONALSHARD.get() && isInfusable(outputStack)) {
                startInfusing();
            }
        }
    }

    private static boolean isInfusable(ItemStack stack) {
        return getStackIfInfusable(stack).map(s -> BaseBlock.getInfused(s) < MachineInfuserConfiguration.MAX_INFUSE.get()).orElse(false);
    }

    @Nonnull
    private static Optional<ItemStack> getStackIfInfusable(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() != 1) {
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
        int rf = infusableHandler.map(h -> (int) (defaultCost * (2.0f - h.getInfusedFactor()) / 2.0f)).orElse(defaultCost);

        if (energyStorage.getEnergy() < rf) {
            // Not enough energy.
            return;
        }
        energyStorage.consumeEnergy(rf);

        items.getStackInSlot(0).split(1);
        if (items.getStackInSlot(0).isEmpty()) {
            items.setStackInSlot(0, ItemStack.EMPTY);
        }
        infusing = 5;
        markDirtyQuick();
    }


    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        infusing = tagCompound.getCompound("Info").getInt("infusing");
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        getOrCreateInfo(tagCompound).putInt("infusing", infusing);
        return tagCompound;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction facing) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energyHandler.cast();
        }
        if (cap == CapabilityContainerProvider.CONTAINER_PROVIDER_CAPABILITY) {
            return screenHandler.cast();
        }
        if (cap == CapabilityInfusable.INFUSABLE_CAPABILITY) {
            return infusableHandler.cast();
        }
        return super.getCapability(cap, facing);
    }
}
