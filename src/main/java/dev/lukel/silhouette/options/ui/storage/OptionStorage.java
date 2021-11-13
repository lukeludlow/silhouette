package dev.lukel.silhouette.options.ui.storage;

public interface OptionStorage<T> {
    T getData();

    void save();
}