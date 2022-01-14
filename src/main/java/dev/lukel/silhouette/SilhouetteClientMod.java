package dev.lukel.silhouette;

import dev.lukel.silhouette.hud.GamertagHudRenderer;
import dev.lukel.silhouette.options.SilhouetteGameOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SilhouetteClientMod implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("silhouette");
    private static SilhouetteGameOptions CONFIG;

    public SilhouetteClientMod() {
        this(new SilhouetteGameOptions());
    }

    // used for DI in unit tests
    public SilhouetteClientMod(SilhouetteGameOptions config) {
        CONFIG = config;
    }


    @Override
    public void onInitializeClient() {
        LOGGER.info("silhouette onInitializeClient");
        CONFIG = loadConfig();

        HudRenderCallback.EVENT.register(new GamertagHudRenderer());
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
