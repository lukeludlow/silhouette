package dev.lukel.silhouette.render;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.entity.player.PlayerEntity;

public class SilhouettePlayerEntity {

    private final PlayerEntity mixin;

    public SilhouettePlayerEntity(PlayerEntity mixin) {
        this.mixin = mixin;
    }

    public boolean isGlowing() {
        return SilhouetteClientMod.options().isEnabled;
    }

    public boolean shouldRenderName() {
        return SilhouetteClientMod.options().displayGamertags;
    }
}
