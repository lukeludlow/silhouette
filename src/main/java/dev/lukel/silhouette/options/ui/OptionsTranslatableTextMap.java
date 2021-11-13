package dev.lukel.silhouette.options.ui;

import java.util.Map;

import static java.util.Map.entry;

public class OptionsTranslatableTextMap {
    public static final String isEnabled = "silhouette.options.enabled";
    public static final String style = "silhouette.options.visual_style";
    public static final String displayGamertags = "silhouette.options.gamertags";
    public static final String shouldRenderWithinCertainRange = "silhouette.options.should_render_within_range";
    public static final String renderDistanceMin = "silhouette.options.render_distance_min";
    public static final String renderDistanceMax = "silhouette.options.render_distance_max";
    public static final String shouldOutlineSelf = "silhouette.options.should_outline_self";

    public static final Map<String, String> map = Map.ofEntries(
            entry("isEnabled", isEnabled),
            entry("style", style),
            entry("displayGamertags", displayGamertags),
            entry("shouldRenderWithinCertainRange", shouldRenderWithinCertainRange),
            entry("renderDistanceMin", renderDistanceMin),
            entry("renderDistanceMax", renderDistanceMax),
            entry("shouldOutlineSelf", shouldOutlineSelf));
}
