package dev.lukel.silhouette.mixin;

import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        if (this.red == 69 && this.green == 69 && this.blue == 69 && this.alpha == 69) {
            ci.cancel();  // don't display outline
        }
//        if (SilhouetteClientMod.options().outlineOnlyWhenFullyHidden) {
//            ci.cancel();
//        } else {
//             continue to the actual method
//        }
    }

}
