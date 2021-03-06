package dev.lukel.silhouette.options;

import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.text.Text;

public enum SilhouetteVisualStyle implements TextProvider {
    MINECRAFT("silhouette.options.visual_style.minecraft"),
    APEX("silhouette.options.visual_style.apex"),
    CUSTOM("silhouette.options.visual_style.custom");

    private final Text name;

    SilhouetteVisualStyle(String name) {
        this.name = Text.translatable(name);
    }

    @Override
    public Text getLocalizedName() {
        return this.name;
    }
}
