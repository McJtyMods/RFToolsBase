package mcjty.rftoolsbase.api.control.machines;

import mcjty.rftoolsbase.api.control.parameters.IParameter;

import javax.annotation.Nullable;

/**
 * A representation of a program
 */
public interface IProgram {

    /**
     * Set a new 'last value' which can be used by future opcodes
     */
    void setLastValue(IParameter value);

    /**
     * Get the current 'last value'
     */
    @Nullable
    IParameter getLastValue();

    /**
     * If this program is running for a craft operation then this will return
     * the current craft ticket.
     */
    @Nullable
    String getCraftTicket();

    boolean hasCraftTicket();

    /**
     * Suspend the program for a specific number of ticks
     */
    void setDelay(int delay);

    /**
     * Return the remaining time before the program resumes
     */
    int getDelay();

    /**
     * Self-destruct. Call this if you want the program to stop
     */
    void killMe();

    /**
     * Return true if the program will stop
     */
    boolean isDead();
}
