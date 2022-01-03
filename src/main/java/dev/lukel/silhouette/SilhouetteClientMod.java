package dev.lukel.silhouette;

import dev.lukel.silhouette.options.SilhouetteGameOptions;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;

public class SilhouetteClientMod implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("silhouette");
    private static SilhouetteGameOptions CONFIG;

    private static final ManagedShaderEffect SILHOUETTE_SHADER = ShaderEffectManager.getInstance().manage(new Identifier("minecraft", "shaders/post/entity_outline.json"));

    public SilhouetteClientMod() {
        this(new SilhouetteGameOptions());
    }

    // used for DI in unit tests
    public SilhouetteClientMod(SilhouetteGameOptions config) {
        CONFIG = config;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("initializing silhouette mod");
        LOGGER.error("sample error");
        CONFIG = loadConfig();


//        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
//            if (CONFIG.silhouette.isEnabled) {
//            }
//        });

    }

    public static SilhouetteGameOptions options() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet available");
        }
        return CONFIG;
    }

    private static SilhouetteGameOptions loadConfig() {
        try {
            return SilhouetteGameOptions.load();
        } catch (Exception e) {
            LOGGER.error("Failed to load configuration file", e);
            LOGGER.error("Using default configuration file in read-only mode");

            var config = new SilhouetteGameOptions();
            config.setReadOnly();

            return config;
        }
    }





}
