package mcjty.rftoolsbase.api.control.parameters;

public interface IParameter {
    boolean isSet();

    ParameterType getParameterType();

    ParameterValue getParameterValue();
}
