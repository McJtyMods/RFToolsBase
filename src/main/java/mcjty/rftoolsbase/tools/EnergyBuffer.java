package mcjty.rftoolsbase.tools;

/** Internal cell accounting; expose energy through {@link EnergyTransactions}. */
public interface EnergyBuffer {
    int receiveEnergy(int amount, boolean simulate);
    int extractEnergy(int amount, boolean simulate);
    int getEnergyStored();
    int getMaxEnergyStored();
    boolean canExtract();
    boolean canReceive();
}
