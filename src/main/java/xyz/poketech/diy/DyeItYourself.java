package xyz.poketech.diy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.poketech.diy.network.DistHelper;
import xyz.poketech.diy.network.PacketHandler;

@Mod.EventBusSubscriber
@Mod(DyeItYourself.MODID)
public final class DyeItYourself {
    public static final String MODID = "diy";

    public static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static ConfigHandler CONFIG;

    public DyeItYourself() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        DIYItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONFIG = ConfigHelper.register(ModConfig.Type.SERVER, ConfigHandler::new);
    }

    public void setup(FMLCommonSetupEvent e) {
        PacketHandler.registerMessages();
    }

    public void clientSetup(FMLClientSetupEvent e) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> DistHelper::addRenderLayers);
    }
}