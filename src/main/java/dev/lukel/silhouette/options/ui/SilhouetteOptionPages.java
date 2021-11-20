package dev.lukel.silhouette.options.ui;

import com.google.common.collect.ImmutableList;
import dev.lukel.silhouette.options.SilhouetteVisualStyle;
import dev.lukel.silhouette.options.ui.control.ControlValueFormatter;
import dev.lukel.silhouette.options.ui.control.CyclingControl;
import dev.lukel.silhouette.options.ui.control.SliderControl;
import dev.lukel.silhouette.options.ui.control.TickBoxControl;
import dev.lukel.silhouette.options.ui.options.OptionFlag;
import dev.lukel.silhouette.options.ui.options.OptionGroup;
import dev.lukel.silhouette.options.ui.options.OptionImpact;
import dev.lukel.silhouette.options.ui.options.OptionPage;
import dev.lukel.silhouette.options.ui.options.OptionUiImpl;
import dev.lukel.silhouette.options.ui.storage.SilhouetteOptionsStorage;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class SilhouetteOptionPages {
    private static final SilhouetteOptionsStorage silhouetteOptions = new SilhouetteOptionsStorage();

    public static OptionPage general() {
        List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionUiImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.isEnabled + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.isEnabled + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.isEnabled = value, opts -> opts.isEnabled)
                        .build())
                .add(OptionUiImpl.createBuilder(SilhouetteVisualStyle.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.style + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.style + ".tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SilhouetteVisualStyle.class))
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.style = value, opts -> opts.style)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build())
                .add(OptionUiImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.displayGamertags + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.displayGamertags + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.displayGamertags = value, opts -> opts.displayGamertags)
                        .build())
                .add(OptionUiImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.shouldOutlineSelf + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.shouldOutlineSelf + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.shouldOutlineSelf = value, opts -> opts.shouldOutlineSelf)
                        .build())
                .add(OptionUiImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.outlineOnlyWhenFullyHidden + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.outlineOnlyWhenFullyHidden + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.HIGH)
                        .setBinding((opts, value) -> opts.outlineOnlyWhenFullyHidden = value, opts -> opts.outlineOnlyWhenFullyHidden)
                        .build())
                .add(OptionUiImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.shouldRenderWithinCertainRange + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.shouldRenderWithinCertainRange + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.shouldRenderWithinCertainRange = value, opts -> opts.shouldRenderWithinCertainRange)
                        .setEnabled(false)  // TODO it's disabled until i implement it
                        .build())
                .add(OptionUiImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.renderDistanceMin + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.renderDistanceMin + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.distance()))
                        .setBinding((opts, value) -> opts.renderDistanceMin = value, opts -> opts.renderDistanceMin)
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(silhouetteOptions.getData().shouldRenderWithinCertainRange)
                        .build())
                .add(OptionUiImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.renderDistanceMax + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.renderDistanceMax + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 1000, 1, ControlValueFormatter.distance()))
                        .setBinding((opts, value) -> opts.renderDistanceMax = value, opts -> opts.renderDistanceMax)
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(silhouetteOptions.getData().shouldRenderWithinCertainRange)
                        .build())
                .build());

        return new OptionPage(new TranslatableText("silhouette.options.general"), ImmutableList.copyOf(groups));
    }
}