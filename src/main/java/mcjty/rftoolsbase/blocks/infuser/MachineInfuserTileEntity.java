package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.api.Infusable;
import mcjty.lib.blocks.GenericBlock;
import mcjty.lib.container.*;
import mcjty.lib.tileentity.GenericEnergyStorage;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.rftoolsbase.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class MachineInfuserTileEntity extends GenericTileEntity implements ITickableTileEntity {

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

    private LazyOptional<NoDirectionItemHander> itemHandler = LazyOptional.of(this::createItemHandler);
    private LazyOptional<GenericEnergyStorage> energyStorage = LazyOptional.of(() -> new GenericEnergyStorage(this, true, MachineInfuserConfiguration.MAXENERGY.get(), MachineInfuserConfiguration.RECEIVEPERTICK.get()));

    private int infusing = 0;

    public MachineInfuserTileEntity() {
        super(MachineInfuserSetup.TYPE_INFUSER);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            tickServer();
        }
    }

    private void tickServer() {
        itemHandler.ifPresent(h -> {
            if (infusing > 0) {
                infusing--;
                if (infusing == 0) {
                    ItemStack outputStack = h.getStackInSlot(1);
                    finishInfusing(outputStack);
                }
                markDirtyQuick();
            } else {
                ItemStack inputStack = h.getStackInSlot(0);
                ItemStack outputStack = h.getStackInSlot(1);
                if (!inputStack.isEmpty() && inputStack.getItem() == ModItems.DIMENSIONALSHARD && isInfusable(outputStack)) {
                    startInfusing();
                }
            }
        });
    }

    private boolean isInfusable(ItemStack stack) {
        return getTagCompound(stack).map(tagCompound -> {
            int infused = tagCompound.getInt("infused");
            return infused < MachineInfuserConfiguration.MAX_INFUSE.get();
        }).orElse(false);
    }

    @Nonnull
    private static Optional<CompoundNBT> getTagCompound(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() != 1) {
            return Optional.empty();
        }

        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) {
            return Optional.empty();
        }
        Block block = ((BlockItem) item).getBlock();
        if (!(block instanceof Infusable || (block instanceof GenericBlock && ((GenericBlock) block).isInfusable()))) {
            return Optional.empty();
        }
        return Optional.of(stack.getOrCreateTag());
    }

    private void finishInfusing(ItemStack stack) {
        getTagCompound(stack).ifPresent(tagCompound -> {
            int infused = tagCompound.getInt("infused");
            tagCompound.putInt("infused", infused + 1);
            stack.setTag(tagCompound);
        });
    }

    private void startInfusing() {
        energyStorage.ifPresent(energy -> {
            int rf = MachineInfuserConfiguration.RFPERTICK.get();
            rf = (int) (rf * (2.0f - getInfusedFactor()) / 2.0f);

            if (energy.getEnergy() < rf) {
                // Not enough energy.
                return;
            }
            energy.consumeEnergy(rf);

            itemHandler.ifPresent(h -> {
                h.getStackInSlot(0).split(1);
                if (h.getStackInSlot(0).isEmpty()) {
                    h.setStackInSlot(0, ItemStack.EMPTY);
                }
            });
            infusing = 5;
            markDirtyQuick();
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction facing) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energyStorage.cast();
        }
        return super.getCapability(cap, facing);
    }

    private NoDirectionItemHander createItemHandler() {
        return new NoDirectionItemHander(MachineInfuserTileEntity.this, CONTAINER_FACTORY, 2) {
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
        itemHandler.ifPresent(h -> h.deserializeNBT(tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND)));
        energyStorage.ifPresent(h -> h.setEnergy(tagCompound.getLong("Energy")));
        infusing = tagCompound.getInt("infusing");
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        itemHandler.ifPresent(h -> tagCompound.put("Items", h.serializeNBT()));
        energyStorage.ifPresent(h -> tagCompound.putLong("Energy", h.getEnergy()));
        tagCompound.putInt("infusing", infusing);
        return tagCompound;
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
        GenericContainer container = new GenericContainer(MachineInfuserSetup.MACHINE_INFUSER_CONTAINER, windowId, MachineInfuserTileEntity.CONTAINER_FACTORY, getPos());
        container.setupInventories(createItemHandler(), inventory);
        energyStorage.ifPresent(e -> e.addIntegerListeners(container));
        return container;
    }

}
