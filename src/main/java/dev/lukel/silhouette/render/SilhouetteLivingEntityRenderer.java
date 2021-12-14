package dev.lukel.silhouette.render;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;

public class SilhouetteLivingEntityRenderer {

    private final LivingEntityRenderer mixin;

    public SilhouetteLivingEntityRenderer(LivingEntityRenderer mixin) {
        this.mixin = mixin;
    }

    public boolean hasLabel(LivingEntity entity) {
        if (entity instanceof OtherClientPlayerEntity) {
            // only override username display if silhouette is running
            return SilhouetteClientMod.options().isEnabled && SilhouetteClientMod.options().displayGamertags;
        }
        return false;
    }
}
