package dev.lukel.silhouette;

public class FinalTriple {
    public float x;
    public float y;
    public boolean isTargetInFrontOfViewPlane;

    public FinalTriple(float x, float y, boolean isTargetInFrontOfViewPlane) {
        this.x = x;
        this.y = y;
        this.isTargetInFrontOfViewPlane = isTargetInFrontOfViewPlane;
    }
}
