package mcjty.rftoolsbase.compat.patchouli;

import mcjty.rftoolsbase.setup.ModSetup;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliCompatibility {

    public static void openBookGUI(ServerPlayerEntity player, ResourceLocation id) {
        if (ModSetup.patchouli) {
            PatchouliAPI.instance.openBookGUI(player, id);
        } else {
            player.sendMessage(new StringTextComponent(TextFormatting.RED + "Patchouli is missing! No manual present"));
        }
    }
}
