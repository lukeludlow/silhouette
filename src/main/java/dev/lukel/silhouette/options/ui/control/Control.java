package dev.lukel.silhouette.options.ui.control;

import dev.lukel.silhouette.options.ui.options.OptionUi;

public interface Control<T> {
    OptionUi<T> getOption();

    ControlElement<T> createElement(Dim2i dim);

    int getMaxWidth();
}