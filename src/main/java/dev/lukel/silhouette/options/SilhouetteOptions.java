package dev.lukel.silhouette.options;

public class SilhouetteOptions {

    public boolean isEnabled = true;
    public SilhouetteVisualStyle style = SilhouetteVisualStyle.APEX;
    public boolean displayGamertags = true;
    public boolean shouldRenderWithinCertainRange = false;
    public int renderDistanceMin = 0;
    public int renderDistanceMax = 0;
    // mainly used for debugging
    public boolean shouldOutlineSelf = false;
    public boolean outlineOnlyWhenFullyHidden = false;
}