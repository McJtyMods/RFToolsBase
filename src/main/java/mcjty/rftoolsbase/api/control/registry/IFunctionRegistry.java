package mcjty.rftoolsbase.api.control.registry;

import mcjty.rftoolsbase.api.control.code.Function;

public interface IFunctionRegistry {

    // Register functions in CommonProxy.init
    void register(Function function);

}
