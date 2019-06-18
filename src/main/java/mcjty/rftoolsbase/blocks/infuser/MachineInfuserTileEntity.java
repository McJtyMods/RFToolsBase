package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.api.Infusable;
import mcjty.lib.blocks.GenericBlock;
import mcjty.lib.container.*;
import mcjty.lib.tileentity.GenericEnergyReceiverTileEntity;
import mcjty.rftoolsbase.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MachineInfuserTileEntity extends GenericEnergyReceiverTileEntity implements ITickableTileEntity {

    public static final int SLOT_SHARDINPUT = 0;
    public static final int SLOT_MACHINEOUTPUT = 1;

    public static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory() {
        @Override
        protected void setup() {
            addSlotBox(new SlotDefinition(SlotType.SLOT_SPECIFICITEM, new ItemStack(ModItems.DIMENSIONALSHARD)), ContainerFactory.CONTAINER_CONTAINER, SLOT_SHARDINPUT, 64, 24, 1, 18, 1, 18);
            addSlotBox(new SlotDefinition(SlotType.SLOT_OUTPUT), ContainerFactory.CONTAINER_CONTAINER, SLOT_MACHINEOUTPUT, 118, 24, 1, 18, 1, 18);
            layoutPlayerInventorySlots(10, 70);
        }
    };
    private InventoryHelper inventoryHelper = new InventoryHelper(this, CONTAINER_FACTORY, 2);

    private int infusing = 0;

    public MachineInfuserTileEntity() {
        super(MachineInfuserSetup.TYPE_INFUSER, MachineInfuserConfiguration.MAXENERGY.get(), MachineInfuserConfiguration.RECEIVEPERTICK.get());
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            checkStateServer();
        }
    }

    private void checkStateServer() {
        if (infusing > 0) {
            infusing--;
            if (infusing == 0) {
                ItemStack outputStack = inventoryHelper.getStackInSlot(1);
                finishInfusing(outputStack);
            }
            markDirty();
        } else {
            ItemStack inputStack = inventoryHelper.getStackInSlot(0);
            ItemStack outputStack = inventoryHelper.getStackInSlot(1);
            if (!inputStack.isEmpty() && inputStack.getItem() == ModItems.DIMENSIONALSHARD && isInfusable(outputStack)) {
                startInfusing();
            }
        }
    }

    private boolean isInfusable(ItemStack stack) {
        CompoundNBT tagCompound = getTagCompound(stack);
        if (tagCompound == null) {
            return false;
        }
        int infused = tagCompound.getInt("infused");
        if (infused >= MachineInfuserConfiguration.MAX_INFUSE.get()) {
            return false;   // Already infused to the maximum.
        }
        return true;
    }

    @Nullable
    private static CompoundNBT getTagCompound(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        if (stack.getCount() != 1) {
            return null;
        }

        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) {
            return null;
        }
        Block block = ((BlockItem)item).getBlock();
        if (!(block instanceof Infusable || (block instanceof GenericBlock && ((GenericBlock) block).isInfusable()))) {
            return null;
        }
        return stack.getOrCreateTag();
    }

    private void finishInfusing(ItemStack stack) {
        CompoundNBT tagCompound = getTagCompound(stack);
        if (tagCompound == null) {
            return;
        }
        int infused = tagCompound.getInt("infused");
        tagCompound.putInt("infused", infused+1);
        stack.setTag(tagCompound);
    }

    private void startInfusing() {
        int rf = MachineInfuserConfiguration.RFPERTICK.get();
        rf = (int) (rf * (2.0f - getInfusedFactor()) / 2.0f);

        if (getStoredPower() < rf) {
            // Not enough energy.
            return;
        }
        consumeEnergy(rf);

        inventoryHelper.getStackInSlot(0).split(1);
        if (inventoryHelper.getStackInSlot(0).isEmpty()) {
            inventoryHelper.setStackInSlot(0, ItemStack.EMPTY);
        }
        infusing = 5;
        markDirty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction facing) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) createItemHandler());
        }
        return super.getCapability(cap, facing);
    }

    private IItemHandler createItemHandler() {
        return new NoDirectionItemHander(inventoryHelper, this) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot != SLOT_SHARDINPUT || stack.getItem() == ModItems.DIMENSIONALSHARD;
            }

            @Override
            public boolean isItemInsertable(int slot, @Nonnull ItemStack stack) {
                return CONTAINER_FACTORY.isInputSlot(slot) || CONTAINER_FACTORY.isSpecificItemSlot(slot);
            }

            @Override
            public boolean isItemExtractable(int slot, @Nonnull ItemStack stack) {
                return CONTAINER_FACTORY.isOutputSlot(slot);
            }
        };
    }


    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
    }

    @Override
    public void readRestorableFromNBT(CompoundNBT tagCompound) {
        super.readRestorableFromNBT(tagCompound);
        readBufferFromNBT(tagCompound, inventoryHelper);
        infusing = tagCompound.getInt("infusing");
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        return tagCompound;
    }

    @Override
    public void writeRestorableToNBT(CompoundNBT tagCompound) {
        super.writeRestorableToNBT(tagCompound);
        writeBufferToNBT(tagCompound, inventoryHelper);
        tagCompound.putInt("infusing", infusing);
    }
}
