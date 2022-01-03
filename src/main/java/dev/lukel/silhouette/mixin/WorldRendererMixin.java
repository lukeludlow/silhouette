package dev.lukel.silhouette.mixin;

import com.google.gson.JsonSyntaxException;
import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteVisualStyle;
import dev.lukel.silhouette.render.PixelRaycast;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.stream.StreamSupport;

import static dev.lukel.silhouette.render.Constants.HIDE_RGBA;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements SynchronousResourceReloader {

    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    private ShaderEffect entityOutlineShader;
    @Shadow
    private Framebuffer entityOutlinesFramebuffer;

    @Shadow
    public void loadEntityOutlineShader() {
    }

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "loadEntityOutlineShader", at = @At("HEAD"), cancellable = true)
    public void silhouette_loadEntityOutlineShader(CallbackInfo ci) {
        SilhouetteClientMod.LOGGER.info("here!!! loadEntityOutlineShader");
        if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.APEX) {
            SilhouetteClientMod.LOGGER.info("silhouette loading apex outline shader");
            loadSilhouetteShader("entity_outline_apex");
            ci.cancel();  // cancel so the regular implementation isn't called
        } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
            SilhouetteClientMod.LOGGER.info("silhouette loading custom outline shader");
            loadSilhouetteShader("entity_outline_custom");
            ci.cancel();  // cancel so the regular implementation isn't called
        } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.MINECRAFT){
            SilhouetteClientMod.LOGGER.info("silhouette loading normal outline shader");
            // continue the function like normal
        }
    }

    private void loadSilhouetteShader(String shaderFileName) {
        if (this.entityOutlineShader != null) {
            this.entityOutlineShader.close();
        }

        Identifier identifier = new Identifier("silhouette", "shaders/post/" + shaderFileName + ".json");

        try {
            this.entityOutlineShader = new ShaderEffect(this.client.getTextureManager(), this.client.getResourceManager(), this.client.getFramebuffer(), identifier);

            this.entityOutlineShader.setupDimensions(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
            this.entityOutlinesFramebuffer = this.entityOutlineShader.getSecondaryTarget("final");
        } catch (IOException var3) {
            SilhouetteClientMod.LOGGER.warn("Failed to load shader: {}", identifier, var3);
            this.entityOutlineShader = null;
            this.entityOutlinesFramebuffer = null;
        } catch (JsonSyntaxException var4) {
            SilhouetteClientMod.LOGGER.warn("Failed to parse shader: {}", identifier, var4);
            this.entityOutlineShader = null;
            this.entityOutlinesFramebuffer = null;
        }
    }


    @Inject(method = "reload()V", at = @At("RETURN"))
    public void silhouette_reload(CallbackInfo ci) {
        SilhouetteClientMod.LOGGER.info("silhouette_reload");
        this.loadEntityOutlineShader();
    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
                              MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {

        if (shouldRenderEntityOutline(entity, vertexConsumers)) {
                OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider) vertexConsumers;
                if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.MINECRAFT) {
                    final int red = 255;
                    final int green = 255;
                    final int blue = 255;
                    final int alpha = 255;
                    outlineVertexConsumers.setColor(red, green, blue, alpha);
                } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.APEX) {
                    final int red = 155;
                    final int green = 215;
                    final int blue = 255;
                    final int alpha = 255;
                    outlineVertexConsumers.setColor(red, green, blue, alpha);
                } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
                    final int red = SilhouetteClientMod.options().customStyle.red;
                    final int green = SilhouetteClientMod.options().customStyle.green;
                    final int blue = SilhouetteClientMod.options().customStyle.blue;
                    final int alpha = SilhouetteClientMod.options().customStyle.alpha;
                    outlineVertexConsumers.setColor(red, green, blue, alpha);
                }
        }

    }

    private boolean shouldRenderEntityOutline(Entity entity, VertexConsumerProvider vertexConsumers) {
        boolean isPlayerEntity = entity instanceof PlayerEntity;
        boolean isOutlineVertexConsumers = vertexConsumers instanceof OutlineVertexConsumerProvider;
        boolean isModEnabled = SilhouetteClientMod.options().silhouette.isEnabled;
        return isPlayerEntity && isOutlineVertexConsumers && isModEnabled;
    }

}
