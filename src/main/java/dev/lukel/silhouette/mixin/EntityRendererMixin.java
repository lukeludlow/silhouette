package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.hud.GamertagHudRenderer;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "shouldRender", at = @At("TAIL"))
    public void shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof OtherClientPlayerEntity) {
            boolean isPlayerOutsideFrustum = !cir.getReturnValue();
            GamertagHudRenderer.setPlayerOutsideFrustum(entity, isPlayerOutsideFrustum);
        }
    }

}
