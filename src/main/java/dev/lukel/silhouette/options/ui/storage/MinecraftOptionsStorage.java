package dev.lukel.silhouette.options.ui.storage;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class MinecraftOptionsStorage implements OptionStorage<GameOptions> {
    private final MinecraftClient client;

    public MinecraftOptionsStorage() {
        this.client = MinecraftClient.getInstance();
    }

    @Override
    public GameOptions getData() {
        return this.client.options;
    }

    @Override
    public void save() {
        this.getData().write();

        SilhouetteClientMod.LOGGER.info("Flushed changes to Minecraft configuration");
    }
}