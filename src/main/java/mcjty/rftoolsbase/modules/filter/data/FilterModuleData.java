package mcjty.rftoolsbase.modules.filter.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record FilterModuleData(List<ItemStack> stacks) {

    public static final Codec<FilterModuleData> CODEC = ItemStack.CODEC.listOf().xmap(FilterModuleData::new, FilterModuleData::stacks);

    public static final FilterModuleData EMPTY = new FilterModuleData(List.of());

    public static final StreamCodec<RegistryFriendlyByteBuf, FilterModuleData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, FilterModuleData::stacks,
            FilterModuleData::new
    );
}
