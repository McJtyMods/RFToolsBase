package mcjty.rftoolsbase;

import mcjty.lib.base.ModBase;
import mcjty.rftoolsbase.client.ClientInfo;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenSetup;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.various.VariousSetup;
import mcjty.rftoolsbase.modules.worldgen.WorldGenSetup;
import mcjty.rftoolsbase.setup.ModSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;


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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        WorldGenSetup.register();
        MachineInfuserSetup.register();
        InformationScreenSetup.register();
        CraftingSetup.register();
        VariousSetup.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent e) -> setup.init(e));

//        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("theoneprobe-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("rftoolsbase-common.toml"));
    }


    @Override
    public String getModId() {
        return RFToolsBase.MODID;
    }

    @Override
    public void openManual(PlayerEntity entityPlayer, int i, String s) {
        // @todo
    }

    public static final String SHIFT_MESSAGE = "<Press Shift>";

    public static Item.Properties createStandardProperties() {
        return new Item.Properties().group(setup.getTab());
    }
}
