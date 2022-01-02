package dev.lukel.silhouette.options;

import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public enum SilhouetteVisualStyle implements TextProvider {
    MINECRAFT("silhouette.options.visual_style.minecraft"),
    APEX("silhouette.options.visual_style.apex");

    private final Text name;

    SilhouetteVisualStyle(String name) {
        this.name = new TranslatableText(name);
    }

    @Override
    public Text getLocalizedName() {
        return this.name;
    }
}
