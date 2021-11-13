package dev.lukel.silhouette.options.ui.options;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public enum OptionImpact implements TextProvider {
    LOW(Formatting.GREEN, "silhouette.option_impact.low"),
    MEDIUM(Formatting.YELLOW, "silhouette.option_impact.medium"),
    HIGH(Formatting.GOLD, "silhouette.option_impact.high"),
    VARIES(Formatting.WHITE, "silhouette.option_impact.varies");

    private final Text text;

    OptionImpact(Formatting color, String text) {
        this.text = new TranslatableText(text).formatted(color);
    }

    @Override
    public Text getLocalizedName() {
        return this.text;
    }
}