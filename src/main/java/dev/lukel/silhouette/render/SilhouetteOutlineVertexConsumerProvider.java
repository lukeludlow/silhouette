package dev.lukel.silhouette.render;

import net.minecraft.client.render.OutlineVertexConsumerProvider;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.lukel.silhouette.render.Constants.HIDE_RGBA;

public class SilhouetteOutlineVertexConsumerProvider {

    private final OutlineVertexConsumerProvider mixin;

    public SilhouetteOutlineVertexConsumerProvider(OutlineVertexConsumerProvider mixin) {
        this.mixin = mixin;
    }

    public void draw(int red, int green, int blue, int alpha, CallbackInfo ci) {
        if (red == HIDE_RGBA && green == HIDE_RGBA && blue == HIDE_RGBA && alpha == HIDE_RGBA) {
            ci.cancel();  // don't display outline
        }
    }
}
