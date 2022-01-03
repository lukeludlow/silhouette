package dev.lukel.silhouette.options;

import java.util.Map;

import static java.util.Map.entry;

public class OptionsTranslatableTextMap {
    public static final String isEnabled = "silhouette.options.enabled";
    public static final String style = "silhouette.options.visual_style";
    public static final String displayGamertags = "silhouette.options.gamertags";
    public static final String shouldRenderWithinCertainRange = "silhouette.options.should_render_within_range";
    public static final String renderDistanceMin = "silhouette.options.render_distance_min";
    public static final String renderDistanceMax = "silhouette.options.render_distance_max";
    public static final String outlineOnlyWhenFullyHidden = "silhouette.options.should_outline_only_when_fully_hidden";
    public static final String red = "silhouette.options.red";
    public static final String green = "silhouette.options.green";
    public static final String blue = "silhouette.options.blue";
    public static final String alpha = "silhouette.options.alpha";

    public static final Map<String, String> map = Map.ofEntries(
            entry("isEnabled", isEnabled),
            entry("style", style),
            entry("displayGamertags", displayGamertags),
            entry("shouldRenderWithinCertainRange", shouldRenderWithinCertainRange),
            entry("renderDistanceMin", renderDistanceMin),
            entry("renderDistanceMax", renderDistanceMax),
            entry("outlineOnlyWhenFullyHidden", outlineOnlyWhenFullyHidden));
}
