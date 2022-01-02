package dev.lukel.silhouette.options;

import com.google.common.collect.ImmutableList;
import dev.lukel.silhouette.SilhouetteClientMod;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

import static me.jellysquid.mods.sodium.client.gui.options.OptionFlag.REQUIRES_RENDERER_RELOAD;

// sodium compat
public class SilhouetteGameOptionPages {

    private static final SilhouetteOptionsStorage silhouetteOptions = new SilhouetteOptionsStorage();

    public static OptionPage silhouette() {
        SilhouetteClientMod.LOGGER.info("SilhouetteGameOptionPages silhouette()");
        List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.isEnabled + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.isEnabled + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.isEnabled = value, opts -> opts.silhouette.isEnabled)
                        .build())
                .add(OptionImpl.createBuilder(SilhouetteVisualStyle.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.style + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.style + ".tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SilhouetteVisualStyle.class))
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.style = value, opts -> opts.silhouette.style)
                        .setFlags(REQUIRES_RENDERER_RELOAD)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, silhouetteOptions)
                        .setName(new TranslatableText(OptionsTranslatableTextMap.displayGamertags + ".name"))
                        .setTooltip(new TranslatableText(OptionsTranslatableTextMap.displayGamertags + ".tooltip"))
                        .setControl(TickBoxControl::new)
                        .setImpact(OptionImpact.LOW)
                        .setBinding((opts, value) -> opts.silhouette.displayGamertags = value, opts -> opts.silhouette.displayGamertags)
                        .build())
                .build());

        return new OptionPage(new TranslatableText("silhouette.options.general"), ImmutableList.copyOf(groups));
    }
}
