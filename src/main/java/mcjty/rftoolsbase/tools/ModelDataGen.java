package mcjty.rftoolsbase.tools;

import mcjty.lib.datagen.model.BlockModelBuilder;
import mcjty.lib.datagen.model.ConfiguredModel;
import mcjty.lib.datagen.model.ModelFile;
import net.minecraft.core.Direction;

/** Small adapters for model JSON supported by the published library builders. */
public final class ModelDataGen {
    private ModelDataGen() { }
    public static ConfiguredModel[] rotated(ModelFile model, Direction direction) {
        int x = direction == Direction.DOWN ? 90 : direction == Direction.UP ? 270 : 0;
        int y = switch (direction) { case SOUTH -> 180; case EAST -> 90; case WEST -> 270; default -> 0; };
        return ConfiguredModel.builder().modelFile(model).rotationX(x).rotationY(y).build();
    }
    public static BlockModelBuilder renderType(BlockModelBuilder model, String type) {
        model.toJson().addProperty("render_type", type);
        return model;
    }
}
