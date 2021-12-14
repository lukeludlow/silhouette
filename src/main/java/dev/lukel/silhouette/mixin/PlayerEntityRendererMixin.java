package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.client.MinecraftClient;
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

import static dev.lukel.silhouette.render.Constants.MINECRAFT_DISPLAY_GAMERTAGS_DISTANCE_LIMIT;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void silhouette_PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
//        super(ctx, new PlayerEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim), 0.5F);
        // FIXME maybe add this feature back idk if i want it
//        this.addFeature(new PlayerSilhouetteFeatureRenderer<>(this, ctx.getModelLoader()));
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    public void silhouette_render(AbstractClientPlayerEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        SilhouetteMod.LOGGER.info("mixin postRender!!!");
    }


    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    protected void renderLabelIfPresent(AbstractClientPlayerEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (SilhouetteClientMod.options().displayGamertags) {
            double d = dispatcher.getSquaredDistanceToCamera(entity);
            boolean isWithinRange = d < ((double) MINECRAFT_DISPLAY_GAMERTAGS_DISTANCE_LIMIT);
            if (isWithinRange) {
//                SilhouetteClientMod.LOGGER.info("silhouette renderLabelIfPresent entity is within normal distance will call normal function");
            } else {
//                SilhouetteClientMod.LOGGER.info("silhouette renderLabelIfPresent entity is too far away so not gonna render sorry");

                // this code is mostly copy pasted from the normal implementation so be careful modifying it

                boolean bl = !entity.isSneaky();
                float f = entity.getHeight() + 0.5F;
                int i = "deadmau5".equals(text.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(0.0D, f, 0.0D);
                matrices.multiply(dispatcher.getRotation());
                float xScale = -0.2f;
                float yScale = xScale;
                float zScale = -xScale;
                matrices.scale(xScale, yScale, zScale);
//                    matrices.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                int j = (int) (g * 255.0F) << 24;
                float h = (float) (-getTextRenderer().getWidth(text) / 2);
                getTextRenderer().draw(text, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
                if (bl) {
                    getTextRenderer().draw(text, h, (float) i, -1, false, matrix4f, vertexConsumers, false, 0, light);
                }
                matrices.pop();
                ci.cancel();
            }
        }
    }
}
