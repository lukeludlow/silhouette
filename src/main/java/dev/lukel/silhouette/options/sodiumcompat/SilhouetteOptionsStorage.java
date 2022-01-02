package dev.lukel.silhouette.options.sodiumcompat;

import dev.lukel.silhouette.SilhouetteClientMod;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;

import java.io.IOException;

public class SilhouetteOptionsStorage implements OptionStorage<SilhouetteGameOptions> {
    private final SilhouetteGameOptions options;

    public SilhouetteOptionsStorage() {
        this.options = SilhouetteClientMod.options();
    }

    @Override
    public SilhouetteGameOptions getData() {
        return this.options;
    }

    @Override
    public void save() {
        try {
            this.options.writeChanges();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save configuration changes", e);
        }

        SilhouetteClientMod.LOGGER.info("flushed changes to silhouette configuration");
    }
}