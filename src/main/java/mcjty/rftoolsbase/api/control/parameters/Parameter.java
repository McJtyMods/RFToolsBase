package mcjty.rftoolsbase.api.control.parameters;

import java.util.Objects;

/**
 * A representation of a parameter.
 */
public class Parameter implements IParameter {

    private final ParameterType parameterType;
    private final ParameterValue parameterValue;

    private Parameter(Builder builder) {
        parameterType = builder.parameterType;
        parameterValue = builder.parameterValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IParameter parameter = (IParameter) o;
        return parameterType == parameter.getParameterType() && Objects.equals(parameterValue, parameter.getParameterValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameterType, parameterValue);
    }

    @Override
    public boolean isSet() {
        return parameterValue != null && parameterValue.getValue() != null;
    }

    @Override
    public ParameterType getParameterType() {
        return parameterType;
    }

    @Override
    public ParameterValue getParameterValue() {
        return parameterValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ParameterType parameterType;
        private ParameterValue parameterValue;

        public Builder type(ParameterType parameterType) {
            this.parameterType = parameterType;
            return this;
        }

        public Builder value(ParameterValue value) {
            this.parameterValue = value;
            return this;
        }

        public Parameter build() {
            return new Parameter(this);
        }

    }
}
