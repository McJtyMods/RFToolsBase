package mcjty.rftoolsbase.modules.filter.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record FilterModuleData(List<ItemStack> stacks, List<ResourceLocation> tags, boolean blacklist, boolean damage, boolean components, boolean mod) {

    public static final Codec<FilterModuleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(FilterModuleData::stacks),
            ResourceLocation.CODEC.listOf().fieldOf("tags").forGetter(FilterModuleData::tags),
            Codec.BOOL.fieldOf("blacklist").forGetter(FilterModuleData::blacklist),
            Codec.BOOL.fieldOf("damage").forGetter(FilterModuleData::damage),
            Codec.BOOL.fieldOf("components").forGetter(FilterModuleData::components),
            Codec.BOOL.fieldOf("mod").forGetter(FilterModuleData::mod)
    ).apply(instance, FilterModuleData::new));

    public static final FilterModuleData EMPTY = new FilterModuleData(List.of(), List.of(), false, false, false, false);

    public static final StreamCodec<RegistryFriendlyByteBuf, FilterModuleData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, FilterModuleData::stacks,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)), FilterModuleData::tags,
            ByteBufCodecs.BOOL, FilterModuleData::blacklist,
            ByteBufCodecs.BOOL, FilterModuleData::damage,
            ByteBufCodecs.BOOL, FilterModuleData::components,
            ByteBufCodecs.BOOL, FilterModuleData::mod,
            FilterModuleData::new
    );
}
