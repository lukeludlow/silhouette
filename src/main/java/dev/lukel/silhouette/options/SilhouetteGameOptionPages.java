package dev.lukel.silhouette.options;

import com.google.common.collect.ImmutableList;
import dev.lukel.silhouette.SilhouetteClientMod;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

import static me.jellysquid.mods.sodium.client.gui.options.OptionFlag.REQUIRES_RENDERER_RELOAD;

// sodium compat
public class SilhouetteGameOptionPages {

    private static final SilhouetteOptionsStorage silhouetteOptions = new SilhouetteOptionsStorage();

    public static OptionPage silhouette() {
        List<OptionGroup> groups = new ArrayList<>();

        // main group
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.isEnabled + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.isEnabled + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.isEnabled = value, opts -> opts.silhouette.isEnabled)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.displayGamertags + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.displayGamertags + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.displayGamertags = value, opts -> opts.silhouette.displayGamertags)
                        .build())
                .add(OptionImpl.createBuilder(SilhouetteVisualStyle.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.style + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.style + ".tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SilhouetteVisualStyle.class))
                        .setBinding((opts, value) -> opts.silhouette.style = value, opts -> opts.silhouette.style)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(REQUIRES_RENDERER_RELOAD)
                        .build())
                .build());

        // group for custom style controls
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(SilhouetteDummyEnum.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.customStyleGroup + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.customStyleGroup + ".tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SilhouetteDummyEnum.class))
                        .setBinding((opts, value) -> opts.customStyle.customStyleGroupTitle = value, opts -> opts.customStyle.customStyleGroupTitle)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.red + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.red + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 255, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.red = value, opts -> opts.customStyle.red)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.green + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.green + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 255, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.green = value, opts -> opts.customStyle.green)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.blue + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.blue + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 255, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.blue = value, opts -> opts.customStyle.blue)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.luminosity + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.luminosity + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 10, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.luminosity = value, opts -> opts.customStyle.luminosity)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(REQUIRES_RENDERER_RELOAD)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.blurDir+ ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.blurDir+ ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.customStyle.blur = value, opts -> opts.customStyle.blur)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(REQUIRES_RENDERER_RELOAD)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.gamertagSize + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.gamertagSize + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 1, 10, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.gamertagSize = value, opts -> opts.customStyle.gamertagSize)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .build());

        return new OptionPage(new TranslatableText("silhouette.options.general"), ImmutableList.copyOf(groups));
    }

}
