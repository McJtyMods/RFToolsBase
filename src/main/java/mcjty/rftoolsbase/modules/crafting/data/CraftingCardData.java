package mcjty.rftoolsbase.modules.crafting.data;

import com.mojang.serialization.Codec;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CraftingCardData(List<ItemStack> stacks) {

    public static final Codec<CraftingCardData> CODEC = ItemStack.CODEC.listOf().xmap(CraftingCardData::new, CraftingCardData::stacks);

    public static final CraftingCardData EMPTY = new CraftingCardData(List.of());

    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingCardData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, CraftingCardData::stacks,
            CraftingCardData::new
    );
}
