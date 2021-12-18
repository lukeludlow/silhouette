package dev.lukel.silhouette;

import dev.lukel.silhouette.options.SilhouetteOptions;
import dev.lukel.silhouette.options.ui.storage.OptionsFileSave;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.util.registry.Registry;
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

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            LOGGER.info("start tracking");
        });

        EntityTrackingEvents.STOP_TRACKING.register((trackedEntity, player) -> {
            LOGGER.info("stop tracking other player entity");
            if (trackedEntity instanceof OtherClientPlayerEntity) {
                LOGGER.info("stop tracking other player entity");
            }
        });


//        ClientPlayNetworking.registerGlobalReceiver()

        RegistryEntryAddedCallback.event(Registry.ENTITY_TYPE).register((((rawId, id, object) -> {

        })));


    }

    // gonna let this stay static even though it's annoying
    public static SilhouetteOptions options() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet available");
        }
        return CONFIG;
    }





}
