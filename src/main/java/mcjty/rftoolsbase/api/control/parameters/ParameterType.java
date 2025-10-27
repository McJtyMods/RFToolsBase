package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.HashMap;
import java.util.Map;

public enum ParameterType {
    PAR_STRING("string"),
    PAR_INTEGER("integer"),
    PAR_FLOAT("float"),
    PAR_SIDE("side"),
    PAR_BOOLEAN("boolean"),
    PAR_INVENTORY("inventory"),
    PAR_ITEM("item"),
    PAR_EXCEPTION("exception"),
    PAR_TUPLE("tuple"),
    PAR_FLUID("fluid"),
    PAR_VECTOR("vector"),
    PAR_LONG("long"),
    PAR_NUMBER("number");

    private final String name;

    private static final Map<String, ParameterType> TYPE_MAP = new HashMap<>();

    public static final Codec<ParameterType> CODEC = Codec.STRING.flatXmap(
            name -> {
                ParameterType type = getByName(name);
                return type != null
                        ? DataResult.success(type)
                        : DataResult.error(() -> "Unknown parameter type: " + name);
            },
            type -> DataResult.success(type.getName()))
            .stable();

    public static final StreamCodec<FriendlyByteBuf, ParameterType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(ParameterType.class);

    static {
        for (ParameterType type : values()) {
            TYPE_MAP.put(type.getName(), type);
        }
    }

    ParameterType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ParameterType getByName(String name) {
        return TYPE_MAP.get(name);
    }
}
