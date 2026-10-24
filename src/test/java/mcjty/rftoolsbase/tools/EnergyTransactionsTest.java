package mcjty.rftoolsbase.tools;

import java.util.List;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnergyTransactionsTest {
    private record Snapshot(int energy, int received) { }
    private static class Store {
        int energy, received;
        final EnergyTransactions.Journal<Snapshot> journal = new EnergyTransactions.Journal<>(
                () -> new Snapshot(energy, received), saved -> { energy = saved.energy(); received = saved.received(); });
        EnergyHandler side() {
            IEnergyStorage legacy = new IEnergyStorage() {
                public int receiveEnergy(int amount, boolean simulate) {
                    int accepted = Math.min(60, Math.min(amount, 100 - energy));
                    if (!simulate) { energy += accepted; received += accepted; }
                    return accepted;
                }
                public int extractEnergy(int amount, boolean simulate) { return 0; }
                public int getEnergyStored() { return energy; }
                public int getMaxEnergyStored() { return 100; }
                public boolean canReceive() { return true; }
                public boolean canExtract() { return false; }
            };
            return EnergyTransactions.input(legacy, () -> List.of(journal));
        }
    }

    @Test void sharedInputsRespectCapacityAndCommit() {
        var store = new Store();
        var first = store.side();
        var second = store.side();
        try (var transaction = Transaction.openRoot()) {
            assertEquals(60, first.insert(80, transaction));
            assertEquals(40, second.insert(80, transaction));
            assertEquals(0, first.insert(1, transaction));
            assertEquals(100, second.getAmountAsLong());
            transaction.commit();
        }
        assertEquals(100, store.energy);
        assertEquals(100, store.received);
    }

    @Test void nestedCommitsAndAbortsRestoreSharedAccounting() {
        var store = new Store();
        var first = store.side();
        var second = store.side();
        try (var outer = Transaction.openRoot()) {
            assertEquals(30, first.insert(30, outer));
            try (var inner = Transaction.open(outer)) {
                assertEquals(25, second.insert(25, inner));
                inner.commit();
            }
            try (var inner = Transaction.open(outer)) { first.insert(20, inner); }
            assertEquals(55, store.energy);
            assertEquals(55, store.received);
        }
        assertEquals(0, store.energy);
        assertEquals(0, store.received);
    }

    @Test void negativeTransfersAreRejectedAndInputStorageCannotExtract() {
        var store = new Store();
        var side = store.side();
        try (var transaction = Transaction.openRoot()) {
            assertThrows(IllegalArgumentException.class, () -> side.insert(-1, transaction));
            assertThrows(IllegalArgumentException.class, () -> side.extract(-1, transaction));
            side.insert(40, transaction);
            assertEquals(0, side.extract(40, transaction));
            transaction.commit();
        }
        assertEquals(40, store.energy);
    }

    @Test void capabilityReportsUnclampedNetworkTotals() {
        IEnergyStorage legacy = new IEnergyStorage() {
            public int receiveEnergy(int amount, boolean simulate) { return 0; }
            public int extractEnergy(int amount, boolean simulate) { return 0; }
            public int getEnergyStored() { return Integer.MAX_VALUE; }
            public int getMaxEnergyStored() { return Integer.MAX_VALUE; }
            public boolean canReceive() { return false; }
            public boolean canExtract() { return false; }
        };
        var side = EnergyTransactions.input(legacy, List::of, () -> 3_000_000_000L, () -> 6_000_000_000L);
        assertEquals(3_000_000_000L, side.getAmountAsLong());
        assertEquals(6_000_000_000L, side.getCapacityAsLong());
    }
}
