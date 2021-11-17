package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.lib.compat.patchouli.PatchouliCompatibility;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nonnull;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;
import static mcjty.lib.builder.TooltipBuilder.key;

public class ManualItem extends Item implements ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header());

    public ManualItem() {
        super(Registration.createStandardProperties().stacksTo(1));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World worldIn, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn) {
        if (!worldIn.isClientSide) {
            PatchouliCompatibility.openBookGUI((ServerPlayerEntity) playerIn, new ResourceLocation(RFToolsBase.MODID, "manual"));
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, World world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flags) {
        super.appendHoverText(itemStack, world, list, flags);
        tooltipBuilder.get().makeTooltip(getRegistryName(), itemStack, list, flags);
    }
}
