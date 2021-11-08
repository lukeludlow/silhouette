package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.PlayerSilhouetteFeatureRenderer;
import dev.lukel.silhouette.SilhouetteMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

//    @Shadow
//    protected PlayerEntityModel<AbstractClientPlayerEntity> model;

//    private PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel;

//    @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory.Context;Z)V", at = @At("RETURN"))
    @Inject(method = "<init>", at = @At("RETURN"))
    public void silhouette_PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
//        super(ctx, new PlayerEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim), 0.5F);
        SilhouetteMod.LOGGER.info("constructor!!!");
        ModelPart bodyModel = ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER);
        PlayerSilhouetteFeatureRenderer feature = new PlayerSilhouetteFeatureRenderer(this, ctx.getModelLoader());
        // temp disabled
//        this.addFeature(feature);
    }
//    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
//        super(ctx, model, shadowRadius);
//    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
    public void silhouette_postRender(AbstractClientPlayerEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//        SilhouetteMod.LOGGER.info("mixin postRender!!!");


//        final int duration = 2000;
//        LivingEntity target = (LivingEntity) entity;
//        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, duration, 0);
//        target.addStatusEffect(statusEffectInstance);

//        final LivingEntity livingEntity = abstractClientPlayerEntity;


        /*
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = this.isVisible(entity);
        boolean bl2 = !bl && !entity.isInvisibleTo(minecraftClient.player);
//        boolean bl3 = minecraftClient.hasOutline(livingEntity);

        final RenderLayer renderLayer = RenderLayer.getOutline(entity.getSkinTexture());

        PlayerEntityModel<AbstractClientPlayerEntity> playerModel = this.model;

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
        int overlay = getOverlay(entity, this.getAnimationCounter(entity, g));
        float red = 1.0f;
        float green = 1.0f;
        float blue = 1.0f;
        float alpha = bl2 ? 0.15f : 1.0f;
        playerModel.render(matrixStack, vertexConsumer, i, overlay, red, green, blue, alpha);

        EntityRenderer<? super AbstractClientPlayerEntity> er = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        */

    }
}
