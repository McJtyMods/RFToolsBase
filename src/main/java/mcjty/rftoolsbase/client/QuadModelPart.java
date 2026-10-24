package mcjty.rftoolsbase.client;

import java.util.List;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;

/** Unculled geometry captured while the block section is rebuilt. */
public record QuadModelPart(List<BakedQuad> quads, Material.Baked particleMaterial) implements BlockStateModelPart {
    public QuadModelPart { quads = List.copyOf(quads); }
    @Override public List<BakedQuad> getQuads(Direction side) { return side == null ? quads : List.of(); }
    @Override public boolean useAmbientOcclusion() { return true; }
    @Override public int materialFlags() { return 0; }
}
