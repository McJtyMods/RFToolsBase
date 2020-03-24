package mcjty.rftoolsbase.api.control.parameters;

public interface IParameter extends Comparable<IParameter> {
    boolean isSet();

    ParameterType getParameterType();

    ParameterValue getParameterValue();
}
