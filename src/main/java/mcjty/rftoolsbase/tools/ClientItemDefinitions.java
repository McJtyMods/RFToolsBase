package mcjty.rftoolsbase.tools;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import mcjty.lib.datagen.model.ModelProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/** Generate the client item definitions for both hand-authored and generated item models. */
public class ClientItemDefinitions implements DataProvider {
    private final PackOutput output;
    private final String modid;
    private final Map<String, JsonObject> overrides;

    public ClientItemDefinitions(PackOutput output, String modid, Map<String, JsonObject> overrides) {
        this.output = output;
        this.modid = modid;
        this.overrides = Map.copyOf(overrides);
    }

    @Override public String getName() { return modid + " client item definitions"; }

    @Override public CompletableFuture<?> run(CachedOutput cache) {
        var models = new LinkedHashMap<String, Identifier>();
        Path generated = output.getOutputFolder();
        Path main = generated.resolve("../../main/resources").normalize();
        try {
            for (Path resources : new Path[]{ main, generated }) {
                Path folder = resources.resolve("assets/" + modid + "/models/item");
                if (!Files.isDirectory(folder)) continue;
                try (var files = Files.walk(folder)) {
                    files.filter(path -> path.toString().endsWith(".json")).forEach(path -> {
                        String relative = folder.relativize(path).toString().replace('\\', '/');
                        String name = relative.substring(0, relative.length() - 5);
                        models.put(name, Identifier.fromNamespaceAndPath(modid, "item/" + name));
                    });
                }
            }
        } catch (IOException error) {
            return CompletableFuture.failedFuture(error);
        }
        var tasks = new ArrayList<CompletableFuture<?>>();
        var paths = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        models.forEach((name, model) -> tasks.add(DataProvider.saveStable(cache,
                overrides.getOrDefault(name, ModelProvider.itemDefinition(model)),
                paths.json(Identifier.fromNamespaceAndPath(modid, name)))));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }
}
