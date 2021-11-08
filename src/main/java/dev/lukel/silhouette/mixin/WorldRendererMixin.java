package dev.lukel.silhouette.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
                              MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (entity instanceof PlayerEntity playerEntity && vertexConsumers instanceof OutlineVertexConsumerProvider outlineVertexConsumers) {
            // TODO find out if player entity is the client's current player
//            playerEntity.player
//            MinecraftClient.getInstance().player.;
            final int red = 255;
            final int green = 255;
            final int blue = 255;
            final int alpha = 255 / 2;
            outlineVertexConsumers.setColor(red, green, blue, alpha);
        }
    }
}
