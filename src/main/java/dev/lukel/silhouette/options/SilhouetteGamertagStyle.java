package dev.lukel.silhouette.options;

import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.text.Text;

public enum SilhouetteGamertagStyle implements TextProvider {
    MINECRAFT("silhouette.options.gamertag_style.minecraft"),
    COLORFUL("silhouette.options.gamertag_style.colorful");

    private final Text name;

    SilhouetteGamertagStyle(String name) {
        this.name = Text.translatable(name);
    }

    @Override
    public Text getLocalizedName() {
        return this.name;
    }
}
