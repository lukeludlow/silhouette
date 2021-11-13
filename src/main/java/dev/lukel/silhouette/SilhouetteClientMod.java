package dev.lukel.silhouette;

import dev.lukel.silhouette.options.ui.storage.OptionsFileSave;
import dev.lukel.silhouette.options.SilhouetteOptions;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SilhouetteClientMod implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("silhouette");
    private static SilhouetteOptions CONFIG;

    public SilhouetteClientMod() {
        this(new SilhouetteOptions());
    }

    // used for DI in unit tests
    public SilhouetteClientMod(SilhouetteOptions config) {
        CONFIG = config;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Hello Fabric world!");
        CONFIG = OptionsFileSave.load();
    }

    // gonna let this stay static even though it's annoying
    public static SilhouetteOptions options() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet available");
        }
        return CONFIG;
    }
}
