package mcjty.rftoolsbase;

import mcjty.lib.base.ModBase;
import mcjty.rftoolsbase.client.ClientInfo;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.setup.ModSetup;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.item.Item;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(RFToolsBase.MODID)
public class RFToolsBase implements ModBase {

    public static final String MODID = "rftoolsbase";

    @SuppressWarnings("PublicField")
    public static ModSetup setup = new ModSetup();

    @SuppressWarnings("PublicField")
    public static RFToolsBase instance;
    public ClientInfo clientInfo = new ClientInfo();

    public RFToolsBase() {
        instance = this;

//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        Registration.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent e) -> setup.init(e));
    }


    @Override
    public String getModId() {
        return RFToolsBase.MODID;
    }

}
