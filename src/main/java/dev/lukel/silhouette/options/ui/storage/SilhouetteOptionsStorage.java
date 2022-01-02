package dev.lukel.silhouette.options.ui.storage;

import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteOptions;

import java.io.IOException;

public class SilhouetteOptionsStorage implements OptionStorage<SilhouetteOptions> {
    private final SilhouetteOptions options;

    public SilhouetteOptionsStorage() {
        this.options = null;
    }

    @Override
    public SilhouetteOptions getData() {
        return this.options;
    }

    @Override
    public void save() {
        try {
            OptionsFileSave.saveChanges(options);
        } catch (IOException e) {
            throw new RuntimeException("couldn't save configuration changes", e);
        }

        SilhouetteClientMod.LOGGER.info("flushed changes to silhouette configuration");
    }
}