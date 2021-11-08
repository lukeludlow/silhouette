package dev.lukel.silhouette;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.mixin.client.rendering.EntityModelLayersAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class PlayerSilhouetteFeatureRenderer<T extends LivingEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {

    private final EntityModel<T> model;

    public PlayerSilhouetteFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, EntityModelLoader loader) {
        super(context);
        ModelPart root = loader.getModelPart(EntityModelLayers.PLAYER_SLIM);
        this.model = new PlayerEntityModel(root, true);
    }

//    @Override
//    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
//    }

//    public abstract void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch);
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = minecraftClient.hasOutline(livingEntity) && livingEntity.isInvisible();
        if (!livingEntity.isInvisible() || bl) {
            VertexConsumer vertexConsumer2;
            bl = true;
            if (bl) {
                vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(this.getTexture(livingEntity)));
            } else {
                vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(livingEntity)));
            }

            final float red = 0.0f;
            final float green = 0.0f;
            final float blue = 1.0f;
            final float alpha = 1.0f;
            final float whiteOverlayProgress = 0.25f;

//            WorldRenderEvents.AFTER_ENTITIES.register(listener -> {
//                listener.
//            });
//            ModelTransform.
//            RenderLayer.getOutline(livingEntity.get)
            ((PlayerEntityModel)this.getContextModel()).copyStateTo(this.model);
            this.model.animateModel(livingEntity, limbAngle, limbDistance, tickDelta);
            this.model.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            matrices.push();
            final float scaleAmount = 1.75f;
            matrices.scale(scaleAmount, scaleAmount, scaleAmount);
            this.model.render(matrices, vertexConsumer2, light, LivingEntityRenderer.getOverlay(livingEntity, whiteOverlayProgress), red, green, blue, alpha);
            matrices.pop();
        }
    }
}
