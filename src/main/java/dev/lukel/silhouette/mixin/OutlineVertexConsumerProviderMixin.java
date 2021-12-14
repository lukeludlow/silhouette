package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.render.SilhouetteOutlineVertexConsumerProvider;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OutlineVertexConsumerProvider.class)
public abstract class OutlineVertexConsumerProviderMixin implements VertexConsumerProvider {

    private final SilhouetteOutlineVertexConsumerProvider impl;

    public OutlineVertexConsumerProviderMixin(VertexConsumerProvider.Immediate parent) {
        this.parent = parent;
        this.impl = new SilhouetteOutlineVertexConsumerProvider((OutlineVertexConsumerProvider) (Object) this);
    }

    @Shadow
    private VertexConsumerProvider.Immediate parent;
    @Shadow
    private int red;
    @Shadow
    private int green;
    @Shadow
    private int blue;
    @Shadow
    private int alpha;

    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    public void draw(CallbackInfo ci) {
        impl.draw(this.red, this.green, this.blue, this.alpha, ci);
    }
}
