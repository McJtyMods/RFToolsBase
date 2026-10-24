package mcjty.rftoolsbase.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.function.Supplier;
import mcjty.lib.varia.LevelTools;
import mcjty.lib.worlddata.AbstractWorldData;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

/** Import pre-26 saved data whose filenames were not valid namespaced identifiers. */
public final class LegacyWorldData {
    private LegacyWorldData() { }

    public static <T extends AbstractWorldData<T>> T get(Level level, Function<CompoundTag, T> loader,
            Supplier<T> factory, String id, String legacyName) {
        return AbstractWorldData.getData(level, loader, () -> {
            var overworld = LevelTools.getOverworld(level);
            var legacy = overworld.getServer().getWorldPath(LevelResource.ROOT)
                    .resolve("data").resolve(legacyName + ".dat");
            if (!Files.isRegularFile(legacy)) return factory.get();
            try {
                var tag = overworld.getDataStorage().readTagFromDisk(legacy, null,
                        SharedConstants.getCurrentVersion().dataVersion().version());
                return loader.apply(tag.getCompoundOrEmpty("data"));
            } catch (IOException error) {
                throw new IllegalStateException("Cannot import saved data " + legacy, error);
            }
        }, id);
    }
}
