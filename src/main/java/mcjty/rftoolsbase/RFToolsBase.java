package mcjty.rftoolsbase;

import mcjty.lib.base.ModBase;
import mcjty.lib.setup.IProxy;
import mcjty.rftoolsbase.setup.ClientProxy;
import mcjty.rftoolsbase.setup.ModSetup;
import mcjty.rftoolsbase.setup.ServerProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(RFToolsBase.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RFToolsBase implements ModBase {

    public static final String MODID = "rftoolsbase";

    @SuppressWarnings("PublicField")
    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    @SuppressWarnings("PublicField")
    public static ModSetup setup = new ModSetup();

    public RFToolsBase() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
    }

    public void init(final FMLCommonSetupEvent event) {
        setup.init(event);
        proxy.init(event);
    }


    @Override
    public String getModId() {
        return RFToolsBase.MODID;
    }

    @Override
    public void openManual(PlayerEntity entityPlayer, int i, String s) {
        // @todo
    }
}
