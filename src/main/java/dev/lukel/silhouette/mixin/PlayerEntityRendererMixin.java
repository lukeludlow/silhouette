package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteVisualStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.sqrt;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    protected void renderLabelIfPresent(AbstractClientPlayerEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (SilhouetteClientMod.options().silhouette.displayGamertags) {
            double distance = dispatcher.getSquaredDistanceToCamera(entity);
            // this code is mostly copy pasted from the normal implementation so be careful modifying it
            float f = entity.getHeight() + 1.0f + ((float)(sqrt(distance * 0.00025f)));
            int i = "deadmau5".equals(text.getString()) ? -10 : 0;
            matrices.push();
            matrices.translate(0.0D, f, 0.0D);
            matrices.multiply(dispatcher.getRotation());
            float xScale = -0.025f;
            float yScale = -0.025f;
            float zScale = 0.025f;
            float scaleUpFactor = 0.0000025f;  // default is 0.0000025f because i think it looks good
            if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
                int gamertagSizeModifier = SilhouetteClientMod.options().customStyle.gamertagSize;
                scaleUpFactor = calculateGamertagScaleUpSize(gamertagSizeModifier);
            }
            float scaleUpSize = (float)sqrt(distance * scaleUpFactor);
            matrices.scale(xScale - scaleUpSize, yScale - scaleUpSize, zScale + scaleUpSize);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = this.getTextRenderer();
            float h = (float)(-textRenderer.getWidth(text) / 2);
//                textRenderer.draw(text, h, (float)i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
            textRenderer.draw(text, h, (float)i, -1, false, matrix4f, vertexConsumers, true, j, light);
            matrices.pop();
            ci.cancel();
        }
        // otherwise, if my gamertags are disabled then continue to the normal function
    }

    private float calculateGamertagScaleUpSize(int gamertagSizeModifier) {
        // this is just a custom formula that i decided looks good
        float minimumScaleUpSize = 0.0000001f;  // calculated by 0.0000025f / (5 * 5)  because 5 is the default
        return minimumScaleUpSize * (gamertagSizeModifier * gamertagSizeModifier);
    }

}
