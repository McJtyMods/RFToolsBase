package mcjty.rftoolsbase.api.control.code;

import mcjty.rftoolsbase.api.control.machines.IProcessor;
import mcjty.rftoolsbase.api.control.machines.IProgram;

public interface IOpcodeRunnable {
    enum OpcodeResult {
        POSITIVE,       // Go to positive end
        NEGATIVE,       // Go to negative end
        HOLD            // Stay at this opcode
    }


    // Return true to process to primary output, else to secondary output
    OpcodeResult run(IProcessor processor, IProgram program, ICompiledOpcode opcode);
}
