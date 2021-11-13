package dev.lukel.silhouette.options.ui.control;

import dev.lukel.silhouette.options.ui.options.OptionUi;
import dev.lukel.silhouette.options.ui.widgets.AbstractWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;

public class ControlElement<T> extends AbstractWidget {
    protected final OptionUi<T> option;

    protected final Dim2i dim;

    protected boolean hovered;

    public ControlElement(OptionUi<T> option, Dim2i dim) {
        this.option = option;
        this.dim = dim;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        String name = this.option.getName().getString();
        String label;

        if (this.hovered && this.font.getWidth(name) > (this.dim.width() - this.option.getControl().getMaxWidth())) {
            name = name.substring(0, Math.min(name.length(), 10)) + "...";
        }

        if (this.option.isAvailable()) {
            if (this.option.hasChanged()) {
                label = Formatting.ITALIC + name + " *";
            } else {
                label = Formatting.WHITE + name;
            }
        } else {
            label = String.valueOf(Formatting.GRAY) + Formatting.STRIKETHROUGH + name;
        }

        this.hovered = this.dim.containsCursor(mouseX, mouseY);

        this.drawRect(this.dim.x(), this.dim.y(), this.dim.getLimitX(), this.dim.getLimitY(), this.hovered ? 0xE0000000 : 0x90000000);
        this.drawString(matrixStack, label, this.dim.x() + 6, this.dim.getCenterY() - 4, 0xFFFFFFFF);
    }

    public OptionUi<T> getOption() {
        return this.option;
    }

    public Dim2i getDimensions() {
        return this.dim;
    }
}