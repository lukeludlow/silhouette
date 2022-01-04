package dev.lukel.silhouette.options;

import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public enum SilhouetteDummyEnum implements TextProvider {
    BLANK("silhouette.options.dummy_enum.blank");

    private final Text name;

    SilhouetteDummyEnum(String name) {
        this.name = new TranslatableText(name);
    }

    @Override
    public Text getLocalizedName() {
        return this.name;
    }
}