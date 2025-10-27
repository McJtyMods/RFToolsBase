package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import mcjty.rftoolsbase.api.control.code.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

/**
 * A value for a parameter. This value can only be
 * interpreted correctly in combination with a ParameterType
 */
public class ParameterValue {

    private final int variableIndex;
    private final Object value;
    private final Function function;

    private static final EnumMap<ParameterType, ConstantSerializer> CONSTANT_SERIALIZERS = new EnumMap<>(ParameterType.class);
    private static final String NUMBER_KIND_KEY = "kind";
    private static final String NUMBER_VALUE_KEY = "value";
    private static final int MAX_STRING_LENGTH = 32767;

    private static final int MODE_CONSTANT = 0;
    private static final int MODE_VARIABLE = 1;
    private static final int MODE_FUNCTION = 2;

    private static FunctionResolver functionResolver = id -> null;

    private ParameterValue(int variableIndex, Object value, Function function) {
        this.variableIndex = variableIndex;
        this.value = value;
        this.function = function;
    }

    public static final Codec<Typed> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<T> encode(Typed input, DynamicOps<T> ops, T prefix) {
            var builder = ops.mapBuilder();
            return ParameterType.CODEC.encodeStart(ops, input.type()).flatMap(typeElement -> {
                builder.add("type", typeElement);
                ParameterValue parameterValue = input.value();
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
                    return encodeConstant(ops, input.type(), constant).flatMap(valueElement -> {
                        builder.add("value", valueElement);
                        return builder.build(prefix);
                    });
                }
            });
        }

        @Override
        public <T> DataResult<com.mojang.datafixers.util.Pair<Typed, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).setLifecycle(com.mojang.serialization.Lifecycle.stable()).flatMap(mapLike -> {
                T typeElement = mapLike.get("type");
                if (typeElement == null) {
                    return DataResult.error(() -> "Missing parameter type");
                }
                return ParameterType.CODEC.parse(ops, typeElement).flatMap(type -> {
                    T variableElement = mapLike.get("variable");
                    if (variableElement != null) {
                        return ops.getNumberValue(variableElement).map(Number::intValue).map(index ->
                                com.mojang.datafixers.util.Pair.of(new Typed(type, ParameterValue.variable(index)), ops.empty()))
                                .mapError(error -> "Invalid variable index: " + error);
                    }
                    T functionElement = mapLike.get("function");
                    if (functionElement != null) {
                        return ops.getStringValue(functionElement).flatMap(id -> {
                                    Function function = resolveFunction(id);
                                    if (function == null) {
                                        return DataResult.error(() -> "Unknown parameter function: " + id);
                                    }
                                    return DataResult.success(com.mojang.datafixers.util.Pair.of(new Typed(type, ParameterValue.function(function)), ops.empty()));
                                })
                                .mapError(error -> "Invalid function id: " + error);
                    }
                    T valueElement = mapLike.get("value");
                    if (valueElement == null) {
                        return DataResult.success(com.mojang.datafixers.util.Pair.of(new Typed(type, ParameterValue.constant(null)), ops.empty()));
                    }
                    return decodeConstant(ops, type, valueElement).flatMap(constant ->
                            DataResult.success(com.mojang.datafixers.util.Pair.of(new Typed(type, ParameterValue.constant(constant)), ops.empty())));
                });
            });
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, Typed> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public Typed decode(RegistryFriendlyByteBuf buf) {
            ParameterType type = ParameterType.STREAM_CODEC.decode(buf);
            int mode = buf.readByte();
            return switch (mode) {
                case MODE_VARIABLE -> new Typed(type, ParameterValue.variable(buf.readVarInt()));
                case MODE_FUNCTION -> {
                    String id = buf.readUtf(MAX_STRING_LENGTH);
                    Function function = resolveFunction(id);
                    if (function == null) {
                        throw new IllegalStateException("Unknown parameter function: " + id);
                    }
                    yield new Typed(type, ParameterValue.function(function));
                }
                case MODE_CONSTANT -> {
                    boolean hasValue = buf.readBoolean();
                    Object value = hasValue ? readConstant(buf, type) : null;
                    yield new Typed(type, ParameterValue.constant(value));
                }
                default -> throw new IllegalStateException("Unsupported parameter value mode: " + mode);
            };
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, Typed input) {
            ParameterType.STREAM_CODEC.encode(buf, input.type());
            ParameterValue parameterValue = input.value();
            if (parameterValue.isVariable()) {
                buf.writeByte(MODE_VARIABLE);
                buf.writeVarInt(parameterValue.getVariableIndex());
            } else if (parameterValue.isFunction()) {
                buf.writeByte(MODE_FUNCTION);
                buf.writeUtf(parameterValue.getFunction().getId(), MAX_STRING_LENGTH);
            } else {
                buf.writeByte(MODE_CONSTANT);
                Object constant = parameterValue.getValue();
                if (constant == null) {
                    buf.writeBoolean(false);
                } else {
                    buf.writeBoolean(true);
                    writeConstant(buf, input.type(), constant);
                }
            }
        }
    };

    public record Typed(ParameterType type, ParameterValue value) {
        public Typed {
            Objects.requireNonNull(type, "Parameter type cannot be null");
            Objects.requireNonNull(value, "Parameter value cannot be null");
        }

        public Parameter toParameter() {
            return Parameter.builder().type(type).value(value).build();
        }

        public static Typed fromParameter(Parameter parameter) {
            return new Typed(parameter.getParameterType(), parameter.getParameterValue());
        }
    }

    public int getVariableIndex() {
        return variableIndex;
    }

    public Object getValue() {
        return value;
    }

    public Function getFunction() {
        return function;
    }

    public boolean isConstant() {
        return variableIndex == -1 && function == null;
    }

    public boolean isVariable() {
        return variableIndex != -1;
    }

    public boolean isFunction() {
        return function != null;
    }

    public static ParameterValue constant(Object value) {
        return new ParameterValue(-1, value, null);
    }

    public static ParameterValue variable(int index) {
        return new ParameterValue(index, null, null);
    }

    public static ParameterValue function(Function function) {
        return new ParameterValue(-1, null, function);
    }

    public static void setFunctionResolver(FunctionResolver resolver) {
        functionResolver = Objects.requireNonNull(resolver);
    }

    public static void registerSerializer(ParameterType type, ConstantSerializer serializer) {
        CONSTANT_SERIALIZERS.put(Objects.requireNonNull(type), Objects.requireNonNull(serializer));
    }

    private static Function resolveFunction(String id) {
        return functionResolver == null ? null : functionResolver.resolve(id);
    }

    private static ConstantSerializer requireSerializer(ParameterType type) {
        ConstantSerializer serializer = CONSTANT_SERIALIZERS.get(type);
        if (serializer == null) {
            throw new IllegalStateException("No serializer registered for parameter type " + type);
        }
        return serializer;
    }

    private static <T> DataResult<T> encodeConstant(DynamicOps<T> ops, ParameterType type, Object value) {
        ConstantSerializer serializer = CONSTANT_SERIALIZERS.get(type);
        if (serializer == null) {
            return DataResult.error(() -> "No serializer registered for parameter type " + type);
        }
        return serializer.encode(ops, value);
    }

    private static <T> DataResult<Object> decodeConstant(DynamicOps<T> ops, ParameterType type, T value) {
        ConstantSerializer serializer = CONSTANT_SERIALIZERS.get(type);
        if (serializer == null) {
            return DataResult.error(() -> "No serializer registered for parameter type " + type);
        }
        return serializer.decode(ops, value);
    }

    private static void writeConstant(RegistryFriendlyByteBuf buf, ParameterType type, Object value) {
        requireSerializer(type).encodeToNetwork(buf, value);
    }

    private static Object readConstant(RegistryFriendlyByteBuf buf, ParameterType type) {
        return requireSerializer(type).decodeFromNetwork(buf);
    }

    private static ConstantSerializer numberSerializer() {
        return new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                if (!(value instanceof Number number)) {
                    return DataResult.error(() -> "Expected Number, got " + value);
                }
                String kind = numberKind(number);
                T kindElement = ops.createString(kind);
                T numberElement = switch (kind) {
                    case "int" -> ops.createInt(number.intValue());
                    case "long" -> ops.createLong(number.longValue());
                    case "float" -> ops.createFloat(number.floatValue());
                    case "double" -> ops.createDouble(number.doubleValue());
                    default -> throw new IllegalStateException("Unknown number kind: " + kind);
                };
                var builder = ops.mapBuilder();
                builder.add(NUMBER_KIND_KEY, kindElement);
                builder.add(NUMBER_VALUE_KEY, numberElement);
                return builder.build(ops.empty());
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return ops.getMap(input).setLifecycle(com.mojang.serialization.Lifecycle.stable()).flatMap(map -> {
                    T kindElement = map.get(NUMBER_KIND_KEY);
                    if (kindElement == null) {
                        return DataResult.error(() -> "Missing number kind");
                    }
                    T valueElement = map.get(NUMBER_VALUE_KEY);
                    if (valueElement == null) {
                        return DataResult.error(() -> "Missing number value");
                    }
                    return ops.getStringValue(kindElement).flatMap(kind -> {
                        return ops.getNumberValue(valueElement).map(number -> switch (kind) {
                            case "int" -> (Object) number.intValue();
                            case "long" -> number.longValue();
                            case "float" -> number.floatValue();
                            case "double" -> number.doubleValue();
                            default -> throw new IllegalStateException("Unknown number kind: " + kind);
                        });
                    });
                });
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                Number number = (Number) value;
                switch (numberKind(number)) {
                    case "int" -> {
                        buf.writeByte(0);
                        buf.writeInt(number.intValue());
                    }
                    case "long" -> {
                        buf.writeByte(1);
                        buf.writeLong(number.longValue());
                    }
                    case "float" -> {
                        buf.writeByte(2);
                        buf.writeFloat(number.floatValue());
                    }
                    case "double" -> {
                        buf.writeByte(3);
                        buf.writeDouble(number.doubleValue());
                    }
                    default -> throw new IllegalStateException("Unknown number kind");
                }
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return switch (buf.readByte()) {
                    case 0 -> buf.readInt();
                    case 1 -> buf.readLong();
                    case 2 -> buf.readFloat();
                    case 3 -> buf.readDouble();
                    default -> throw new IllegalStateException("Unknown number kind");
                };
            }
        };
    }

    private static String numberKind(Number number) {
        if (number instanceof Integer || number instanceof Short || number instanceof Byte) {
            return "int";
        } else if (number instanceof Long) {
            return "long";
        } else if (number instanceof Float) {
            return "float";
        } else if (number instanceof Double) {
            return "double";
        }
        return "double";
    }

    static {
        registerSerializer(ParameterType.PAR_STRING, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return DataResult.success(ops.createString((String) value));
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return ops.getStringValue(input).map(s -> (Object) s);
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                buf.writeUtf((String) value, MAX_STRING_LENGTH);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return buf.readUtf(MAX_STRING_LENGTH);
            }
        });

        registerSerializer(ParameterType.PAR_INTEGER, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return DataResult.success(ops.createInt((Integer) value));
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return ops.getNumberValue(input).map(number -> (Object) number.intValue());
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                buf.writeInt((Integer) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return buf.readInt();
            }
        });

        registerSerializer(ParameterType.PAR_LONG, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return DataResult.success(ops.createLong((Long) value));
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return ops.getNumberValue(input).map(number -> (Object) number.longValue());
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                buf.writeLong((Long) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return buf.readLong();
            }
        });

        registerSerializer(ParameterType.PAR_FLOAT, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return DataResult.success(ops.createFloat((Float) value));
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return ops.getNumberValue(input).map(number -> (Object) number.floatValue());
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                buf.writeFloat((Float) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return buf.readFloat();
            }
        });

        registerSerializer(ParameterType.PAR_BOOLEAN, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return DataResult.success(ops.createBoolean((Boolean) value));
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return ops.getBooleanValue(input).map(bool -> (Object) bool);
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                buf.writeBoolean((Boolean) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return buf.readBoolean();
            }
        });

        registerSerializer(ParameterType.PAR_SIDE, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return BlockSide.CODEC.encodeStart(ops, (BlockSide) value);
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return BlockSide.CODEC.parse(ops, input).map(side -> (Object) side);
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                BlockSide.STREAM_CODEC.encode(buf, (BlockSide) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return BlockSide.STREAM_CODEC.decode(buf);
            }
        });

        registerSerializer(ParameterType.PAR_INVENTORY, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return Inventory.CODEC.encodeStart(ops, (Inventory) value);
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return Inventory.CODEC.parse(ops, input).map(inv -> (Object) inv);
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                Inventory.STREAM_CODEC.encode(buf, (Inventory) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return Inventory.STREAM_CODEC.decode(buf);
            }
        });

        registerSerializer(ParameterType.PAR_ITEM, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return ItemStack.CODEC.encodeStart(ops, (ItemStack) value);
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return ItemStack.CODEC.parse(ops, input).map(stack -> (Object) stack);
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                ItemStack.STREAM_CODEC.encode(buf, (ItemStack) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return ItemStack.STREAM_CODEC.decode(buf);
            }
        });

        registerSerializer(ParameterType.PAR_FLUID, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return FluidStack.CODEC.encodeStart(ops, (FluidStack) value);
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return FluidStack.CODEC.parse(ops, input).map(stack -> (Object) stack);
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                FluidStack.STREAM_CODEC.encode(buf, (FluidStack) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return FluidStack.STREAM_CODEC.decode(buf);
            }
        });

        registerSerializer(ParameterType.PAR_TUPLE, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                return Tuple.CODEC.encodeStart(ops, (Tuple) value);
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return Tuple.CODEC.parse(ops, input).map(tuple -> (Object) tuple);
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                Tuple.STREAM_CODEC.encode(buf, (Tuple) value);
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                return Tuple.STREAM_CODEC.decode(buf);
            }
        });

        registerSerializer(ParameterType.PAR_VECTOR, new ConstantSerializer() {
            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
                @SuppressWarnings("unchecked")
                List<Parameter> list = (List<Parameter>) value;
                return Parameter.CODEC.listOf().encodeStart(ops, list);
            }

            @Override
            public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
                return Parameter.CODEC.listOf().parse(ops, input).map(list -> (Object) List.copyOf(list));
            }

            @Override
            public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
                @SuppressWarnings("unchecked")
                List<Parameter> list = (List<Parameter>) value;
                buf.writeInt(list.size());
                for (Parameter parameter : list) {
                    Parameter.STREAM_CODEC.encode(buf, parameter);
                }
            }

            @Override
            public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
                int size = buf.readInt();
                java.util.ArrayList<Parameter> list = new java.util.ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    list.add(Parameter.STREAM_CODEC.decode(buf));
                }
                return List.copyOf(list);
            }
        });

        registerSerializer(ParameterType.PAR_NUMBER, numberSerializer());
    }

    public interface ConstantSerializer {
        <T> DataResult<T> encode(DynamicOps<T> ops, Object value);

        <T> DataResult<Object> decode(DynamicOps<T> ops, T input);

        void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value);

        Object decodeFromNetwork(RegistryFriendlyByteBuf buf);
    }

    @FunctionalInterface
    public interface FunctionResolver {
        @Nullable
        Function resolve(String id);
    }
}
