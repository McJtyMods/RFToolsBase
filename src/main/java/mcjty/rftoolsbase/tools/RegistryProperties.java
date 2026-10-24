package mcjty.rftoolsbase.tools;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class RegistryProperties {
    private RegistryProperties() { }
    /** Retain the library's standard machine settings on properties carrying the registry ID. */
    public static BlockBehaviour.Properties block(BlockBehaviour.Properties properties) {
        return properties.mapColor(MapColor.METAL).strength(2F).requiresCorrectToolForDrops().sound(SoundType.METAL);
    }
}
