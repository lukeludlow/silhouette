package dev.lukel.silhouette.mixin;

import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.lukel.silhouette.render.Constants.HIDE_RGBA;

@Mixin(OutlineVertexConsumerProvider.class)
public abstract class OutlineVertexConsumerProviderMixin implements VertexConsumerProvider {

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
        if (red == HIDE_RGBA && green == HIDE_RGBA && blue == HIDE_RGBA && alpha == HIDE_RGBA) {
            ci.cancel();  // don't display outline
        }
    }
}
