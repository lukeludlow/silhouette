package dev.lukel.silhouette.options.ui.options;

import dev.lukel.silhouette.options.ui.control.Control;
import dev.lukel.silhouette.options.ui.storage.OptionStorage;
import net.minecraft.text.Text;

import java.util.Collection;

public interface OptionUi<T> {
    Text getName();

    Text getTooltip();

    OptionImpact getImpact();

    Control<T> getControl();

    T getValue();

    void setValue(T value);

    void reset();

    OptionStorage<?> getStorage();

    boolean isAvailable();

    boolean hasChanged();

    void applyChanges();

    Collection<OptionFlag> getFlags();
}