package mcjty.rftoolsbase.modules.tablet.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record TabletData(List<ItemStack> stacks, int current) {

    public static final TabletData EMPTY = new TabletData(List.of(), 0);

    public static final Codec<TabletData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(TabletData::stacks),
            Codec.INT.fieldOf("current").forGetter(TabletData::current)
    ).apply(instance, TabletData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TabletData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, TabletData::stacks,
            ByteBufCodecs.INT, TabletData::current,
            TabletData::new
    );

    public TabletData withCurrent(int current) {
        return new TabletData(stacks(), current);
    }

    public TabletData setStack(int index, ItemStack stack) {
        List<ItemStack> newStacks = stacks();
        if (newStacks.isEmpty()) {
            newStacks = new ArrayList<>();
            for (int i = 0 ; i < TabletContainer.NUM_SLOTS ; i++) {
                newStacks.add(ItemStack.EMPTY);
            }
        }
        newStacks.set(index, stack);
        return new TabletData(newStacks, current());
    }
}
