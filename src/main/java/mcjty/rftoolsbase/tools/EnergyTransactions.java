package mcjty.rftoolsbase.tools;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import mcjty.lib.varia.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

/** Transactional access to the cells' existing shared energy accounting. */
public final class EnergyTransactions {
    private EnergyTransactions() { }

    public static final class Journal<S> extends SnapshotParticipant<S> {
        private final Supplier<S> capture;
        private final Consumer<S> restore;
        public Journal(Supplier<S> capture, Consumer<S> restore) {
            this.capture = capture;
            this.restore = restore;
        }
        @Override protected S createSnapshot() { return capture.get(); }
        @Override protected void readSnapshot(S snapshot) { restore.accept(snapshot); }
    }

    public static EnergyStorage input(IEnergyStorage storage, Supplier<List<SnapshotParticipant<?>>> journals) {
        if (storage == null) return null;
        return input(storage, journals, storage::getEnergyStored, storage::getMaxEnergyStored);
    }

    public static EnergyStorage input(IEnergyStorage storage, Supplier<List<SnapshotParticipant<?>>> journals,
            java.util.function.LongSupplier energy, java.util.function.LongSupplier capacity) {
        if (storage == null) return null;
        return new EnergyStorage() {
            @Override public boolean supportsInsertion() { return storage.canReceive(); }
            @Override public boolean supportsExtraction() { return false; }
            @Override public long getAmount() { return energy.getAsLong(); }
            @Override public long getCapacity() { return capacity.getAsLong(); }
            @Override public long insert(long amount, TransactionContext transaction) {
                if (amount < 0) throw new IllegalArgumentException("Negative energy transfer");
                if (amount == 0 || !storage.canReceive() || storage.receiveEnergy((int)Math.min(amount, Integer.MAX_VALUE), true) <= 0) return 0;
                for (var journal : journals.get()) journal.updateSnapshots(transaction);
                return storage.receiveEnergy((int)Math.min(amount, Integer.MAX_VALUE), false);
            }
            // These three capability providers have always accepted energy input only.
            @Override public long extract(long amount, TransactionContext transaction) {
                if (amount < 0) throw new IllegalArgumentException("Negative energy transfer");
                return 0;
            }
        };
    }
}
