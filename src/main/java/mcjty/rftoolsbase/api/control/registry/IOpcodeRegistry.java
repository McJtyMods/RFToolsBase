package mcjty.rftoolsbase.api.control.registry;

import mcjty.rftoolsbase.api.control.code.Opcode;

public interface IOpcodeRegistry {

    // Register opcodes in CommonProxy.init
    void register(Opcode opcode);

}
