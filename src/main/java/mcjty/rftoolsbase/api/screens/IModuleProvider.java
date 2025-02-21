package mcjty.rftoolsbase.api.screens;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Implement this interface on your module item.
 */
public interface IModuleProvider {

    Codec<? extends IScreenModule<?>> codec();

    StreamCodec<RegistryFriendlyByteBuf, ? extends IScreenModule<?>> streamCodec();

    Class<? extends IScreenModule<?>> getServerScreenModule();

    Class<? extends IClientScreenModule<?>> getClientScreenModule();

    String getModuleName();

    void createGui(IModuleGuiBuilder guiBuilder);
}
