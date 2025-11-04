package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import mcjty.rftoolsbase.api.control.code.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

import static mcjty.rftoolsbase.api.control.parameters.ParameterValue.*;

/**
 * A representation of a parameter.
 */
public class Parameter implements IParameter {

    private final ParameterType parameterType;
    private final ParameterValue parameterValue;

    private static final int MODE_FUNCTION = 2;
    private static final int MODE_VARIABLE = 1;
    private static final int MODE_CONSTANT = 0;

    public static final Codec<Parameter> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<T> encode(Parameter input, DynamicOps<T> ops, T prefix) {
            var builder = ops.mapBuilder();
            return ParameterType.CODEC.encodeStart(ops, input.getParameterType()).flatMap(typeElement -> {
                builder.add("type", typeElement);
                ParameterValue parameterValue = input.getParameterValue();
                if (parameterValue == null) {
                    return DataResult.error(() -> "Parameter value is missing");
                }
                if (parameterValue.isVariable()) {
                    builder.add("variable", ops.createInt(parameterValue.getVariableIndex()));
                    return builder.build(prefix);
                } else if (parameterValue.isFunction()) {
                    builder.add("function", ops.createString(parameterValue.getFunction().getId()));
                    return builder.build(prefix);
                } else {
                    Object constant = parameterValue.getValue();
                    if (constant == null) {
                        return builder.build(prefix);
                    }
                    return ParameterSerializerHelpers.encodeConstant(ops, input.getParameterType(), constant).flatMap(valueElement -> {
                        builder.add("value", valueElement);
                        return builder.build(prefix);
                    });
                }
            });
        }

        @Override
        public <T> DataResult<Pair<Parameter, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(mapLike -> {
                T typeElement = mapLike.get("type");
                if (typeElement == null) {
                    return DataResult.error(() -> "Missing parameter type");
                }
                return ParameterType.CODEC.parse(ops, typeElement).flatMap(type -> {
                    T variableElement = mapLike.get("variable");
                    if (variableElement != null) {
                        return ops.getNumberValue(variableElement).map(Number::intValue).map(index ->
                                        Pair.of(new Parameter(type, ParameterValue.variable(index)), ops.empty()))
                                .mapError(error -> "Invalid variable index: " + error);
                    }
                    T functionElement = mapLike.get("function");
                    if (functionElement != null) {
                        return ops.getStringValue(functionElement).flatMap(id -> {
                                    Function function = ParameterSerializerHelpers.resolveFunction(id);
                                    if (function == null) {
                                        return DataResult.error(() -> "Unknown parameter function: " + id);
                                    }
                                    return DataResult.success(Pair.of(new Parameter(type, function(function)), ops.empty()));
                                })
                                .mapError(error -> "Invalid function id: " + error);
                    }
                    T valueElement = mapLike.get("value");
                    if (valueElement == null) {
                        return DataResult.success(Pair.of(new Parameter(type, ParameterValue.constant(null)), ops.empty()));
                    }
                    return ParameterSerializerHelpers.decodeConstant(ops, type, valueElement).flatMap(constant ->
                            DataResult.success(Pair.of(new Parameter(type, ParameterValue.constant(constant)), ops.empty())));
                });
            });
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, Parameter> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public Parameter decode(RegistryFriendlyByteBuf buf) {
            ParameterType type = ParameterType.STREAM_CODEC.decode(buf);
            int mode = buf.readByte();
            return switch (mode) {
                case MODE_VARIABLE -> new Parameter(type, ParameterValue.variable(buf.readVarInt()));
                case MODE_FUNCTION -> {
                    String id = buf.readUtf();
                    Function function = ParameterSerializerHelpers.resolveFunction(id);
                    if (function == null) {
                        throw new IllegalStateException("Unknown parameter function: " + id);
                    }
                    yield new Parameter(type, ParameterValue.function(function));
                }
                case MODE_CONSTANT -> {
                    boolean hasValue = buf.readBoolean();
                    Object value = hasValue ? ParameterSerializerHelpers.requireSerializer(type).decodeFromNetwork(buf) : null;
                    yield new Parameter(type, ParameterValue.constant(value));
                }
                default -> throw new IllegalStateException("Unsupported parameter value mode: " + mode);
            };
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, Parameter input) {
            ParameterType.STREAM_CODEC.encode(buf, input.getParameterType());
            ParameterValue parameterValue = input.getParameterValue();
            if (parameterValue.isVariable()) {
                buf.writeByte(MODE_VARIABLE);
                buf.writeVarInt(parameterValue.getVariableIndex());
            } else if (parameterValue.isFunction()) {
                buf.writeByte(MODE_FUNCTION);
                buf.writeUtf(parameterValue.getFunction().getId());
            } else {
                buf.writeByte(MODE_CONSTANT);
                Object constant = parameterValue.getValue();
                if (constant == null) {
                    buf.writeBoolean(false);
                } else {
                    buf.writeBoolean(true);
                    ParameterType type = input.getParameterType();
                    ParameterSerializerHelpers.requireSerializer(type).encodeToNetwork(buf, constant);
                }
            }
        }
    };

    private Parameter(ParameterType parameterType, ParameterValue parameterValue) {
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }

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
