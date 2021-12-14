package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.render.SilhouettePlayerEntityRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final SilhouettePlayerEntityRenderer impl;

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
        this.impl = new SilhouettePlayerEntityRenderer();
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
        impl.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light, this.dispatcher, this.getTextRenderer(), ci);
    }
}
