package dev.lukel.silhouette;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

// note: T is AbstractClientPlayerEntity but i have to do it this weird way to fit with the other feature renderers
public class PlayerSilhouetteFeatureRenderer<T extends LivingEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {

    private final EntityModel<T> model;

    public PlayerSilhouetteFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, EntityModelLoader loader) {
        super(context);
        ModelPart root = loader.getModelPart(EntityModelLayers.PLAYER_SLIM);
        this.model = new PlayerEntityModel<>(root, true);
    }


    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        //SilhouetteMod.LOGGER.info(String.format("entity type = %s, vertex type = %s", livingEntity.getClass(), vertexConsumers.getClass()));

        // OutlineVertexConsumerProvider when isGlowing
        // otherwise it's VertexConsumerProvider$Immediate

        RenderLayer renderLayer = RenderLayer.getOutline(getTexture(livingEntity));
        final VertexConsumer outlineVertexConsumer = vertexConsumers.getBuffer(RenderLayer.getOutline(getTexture(livingEntity)));
        this.getContextModel().copyStateTo(this.model);

        // render basic minecraft outline glow thing OR render an outline that looks like apex legends
        boolean displayMinecraftStyle = true;
        if (displayMinecraftStyle) {
            final int red = 0;
            final int green = 255;
            final int blue = 255;
            final int alpha = 255;
            if (vertexConsumers instanceof OutlineVertexConsumerProvider outlineVertexProvider) {
                outlineVertexProvider.setColor(red, green, blue, alpha);
            } else if (vertexConsumers instanceof VertexConsumerProvider.Immediate immediateVertexProvider) {
                VertexConsumer buffer = immediateVertexProvider.getBuffer(renderLayer);
            }
        } else {
            // apex style
            final float red = 0.0f;
            final float green = 1.0f;
            final float blue = 1.0f;
            final float alpha = 0.1f;
            final float whiteOverlayProgress = 1.0f;

            this.model.animateModel(livingEntity, limbAngle, limbDistance, tickDelta);
            this.model.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
//            matrices.push();
//            final float scaleAmount = 1.75f;
//            matrices.scale(scaleAmount, scaleAmount, scaleAmount);
//            this.model.render(matrices, outlineVertexConsumer, light, LivingEntityRenderer.getOverlay(livingEntity, whiteOverlayProgress), red, green, blue, alpha);
            this.model.render(matrices, outlineVertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
//            matrices.pop();
        }

    }
}
