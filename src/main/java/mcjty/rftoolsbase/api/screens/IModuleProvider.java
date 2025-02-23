package mcjty.rftoolsbase.api.screens;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.Nullable;

/**
 * Implement this interface on your module item.
 */
public interface IModuleProvider {

    @Nullable
    Codec<? extends IScreenModule<?>> codec();

    @Nullable
    StreamCodec<RegistryFriendlyByteBuf, ? extends IScreenModule<?>> streamCodec();

    @Nullable
    DataComponentType<? extends IScreenModule<?>> componentType();

    IScreenModule<?> createServerScreenModule();

    @Nullable
    Codec<? extends IClientScreenModule<?>> clientCodec();

    @Nullable
    StreamCodec<RegistryFriendlyByteBuf, ? extends IClientScreenModule<?>> clientStreamCodec();

    @Nullable
    DataComponentType<? extends IClientScreenModule<?>> clientComponentType();

    IClientScreenModule<?> createClientScreenModule();

    String getModuleName();

    void createGui(IModuleGuiBuilder guiBuilder);
}
