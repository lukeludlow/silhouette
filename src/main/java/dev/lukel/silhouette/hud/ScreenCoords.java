package dev.lukel.silhouette.hud;

public class ScreenCoords {
    public float x;
    public float y;
    public boolean isTargetInFrontOfViewPlane;

    public ScreenCoords(float x, float y, boolean isTargetInFrontOfViewPlane) {
        this.x = x;
        this.y = y;
        this.isTargetInFrontOfViewPlane = isTargetInFrontOfViewPlane;
    }
}
