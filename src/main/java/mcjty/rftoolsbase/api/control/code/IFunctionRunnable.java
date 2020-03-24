package mcjty.rftoolsbase.api.control.code;

import mcjty.rftoolsbase.api.control.machines.IProcessor;
import mcjty.rftoolsbase.api.control.machines.IProgram;

public interface IFunctionRunnable {
    Object run(IProcessor processor, IProgram program);
}
