package dev.lukel.silhouette.options;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.text.Text;

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
                        .setName(Text.translatable(OptionsTranslatableTextMap.isEnabled + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.isEnabled + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.isEnabled = value, opts -> opts.silhouette.isEnabled)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.displayGamertags + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.displayGamertags + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.displayGamertags = value, opts -> opts.silhouette.displayGamertags)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.insaneDistance + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.insaneDistance + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.insaneDistance = value, opts -> opts.silhouette.insaneDistance)
                        .build())
                .add(OptionImpl.createBuilder(SilhouetteGamertagStyle.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.gamertagStyle + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.gamertagStyle + ".tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SilhouetteGamertagStyle.class))
                        .setBinding((opts, value) -> opts.silhouette.gamertagStyle = value, opts -> opts.silhouette.gamertagStyle)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(SilhouetteVisualStyle.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.style + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.style + ".tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SilhouetteVisualStyle.class))
                        .setBinding((opts, value) -> opts.silhouette.style = value, opts -> opts.silhouette.style)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(REQUIRES_RENDERER_RELOAD)
                        .build())
                .build());

        // group for custom style controls
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(SilhouetteDummyEnum.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.customStyleGroup + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.customStyleGroup + ".tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SilhouetteDummyEnum.class))
                        .setBinding((opts, value) -> opts.customStyle.customStyleGroupTitle = value, opts -> opts.customStyle.customStyleGroupTitle)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.red + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.red + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 255, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.red = value, opts -> opts.customStyle.red)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.green + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.green + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 255, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.green = value, opts -> opts.customStyle.green)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.blue + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.blue + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 255, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.blue = value, opts -> opts.customStyle.blue)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.luminosity + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.luminosity + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 10, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.luminosity = value, opts -> opts.customStyle.luminosity)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(REQUIRES_RENDERER_RELOAD)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.blurDir+ ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.blurDir+ ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.customStyle.blur = value, opts -> opts.customStyle.blur)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(REQUIRES_RENDERER_RELOAD)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.gamertagSize + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.gamertagSize + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 1, 20, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.gamertagSize = value, opts -> opts.customStyle.gamertagSize)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .add(OptionImpl.createBuilder(int.class, silhouetteOptions)
                        .setName(Text.translatable(OptionsTranslatableTextMap.insaneDistanceGamertagSize + ".name"))
                        .setTooltip(Text.translatable(OptionsTranslatableTextMap.insaneDistanceGamertagSize + ".tooltip"))
                        .setControl(option -> new SliderControl(option, 1, 20, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customStyle.insaneDistanceGamertagSize = value, opts -> opts.customStyle.insaneDistanceGamertagSize)
                        .setImpact(OptionImpact.LOW)
                        .build())
                .build());

        return new OptionPage(Text.translatable("silhouette.options.general"), ImmutableList.copyOf(groups));
    }

}
