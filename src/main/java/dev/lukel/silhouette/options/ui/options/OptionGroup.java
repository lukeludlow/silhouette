package dev.lukel.silhouette.options.ui.options;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

public class OptionGroup {

    private final ImmutableList<OptionUi<?>> options;

    private OptionGroup(ImmutableList<OptionUi<?>> options) {
        this.options = options;
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public ImmutableList<OptionUi<?>> getOptions() {
        return this.options;
    }

    public static class Builder {
        private final List<OptionUi<?>> options = new ArrayList<>();

        public Builder add(OptionUi<?> option) {
            this.options.add(option);
            return this;
        }

        public OptionGroup build() {
            Validate.notEmpty(this.options, "At least one option must be specified");
            return new OptionGroup(ImmutableList.copyOf(this.options));
        }
    }
}