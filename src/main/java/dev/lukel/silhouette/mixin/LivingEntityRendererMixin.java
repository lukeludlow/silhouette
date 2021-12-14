package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.render.SilhouetteLivingEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//public abstract class LivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    private final SilhouetteLivingEntityRenderer impl;

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.impl = new SilhouetteLivingEntityRenderer((LivingEntityRenderer) (Object) this);
    }

    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    protected void hasLabel(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (impl.hasLabel(livingEntity)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
        // continue to normal implementation
    }
}
