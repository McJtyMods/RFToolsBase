package mcjty.rftoolsbase.tools;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/** Transactional access to the cells' existing shared energy accounting. */
public final class EnergyTransactions {
    private EnergyTransactions() { }

    public static final class Journal<S> extends SnapshotJournal<S> {
        private final Supplier<S> capture;
        private final Consumer<S> restore;
        public Journal(Supplier<S> capture, Consumer<S> restore) {
            this.capture = capture;
            this.restore = restore;
        }
        @Override protected S createSnapshot() { return capture.get(); }
        @Override protected void revertToSnapshot(S snapshot) { restore.accept(snapshot); }
    }

    public static EnergyHandler input(IEnergyStorage storage, Supplier<List<SnapshotJournal<?>>> journals) {
        if (storage == null) return null;
        return input(storage, journals, storage::getEnergyStored, storage::getMaxEnergyStored);
    }

    public static EnergyHandler input(IEnergyStorage storage, Supplier<List<SnapshotJournal<?>>> journals,
            java.util.function.LongSupplier energy, java.util.function.LongSupplier capacity) {
        if (storage == null) return null;
        return new EnergyHandler() {
            @Override public long getAmountAsLong() { return energy.getAsLong(); }
            @Override public long getCapacityAsLong() { return capacity.getAsLong(); }
            @Override public int insert(int amount, TransactionContext transaction) {
                if (amount < 0) throw new IllegalArgumentException("Negative energy transfer");
                if (amount == 0 || !storage.canReceive() || storage.receiveEnergy(amount, true) <= 0) return 0;
                for (var journal : journals.get()) journal.updateSnapshots(transaction);
                return storage.receiveEnergy(amount, false);
            }
            // These three capability providers have always accepted energy input only.
            @Override public int extract(int amount, TransactionContext transaction) {
                if (amount < 0) throw new IllegalArgumentException("Negative energy transfer");
                return 0;
            }
        };
    }
}
