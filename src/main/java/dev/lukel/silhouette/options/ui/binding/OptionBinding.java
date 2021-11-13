package dev.lukel.silhouette.options.ui.binding;

public interface OptionBinding<S, T> {
    void setValue(S storage, T value);

    T getValue(S storage);
}