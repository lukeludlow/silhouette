package dev.lukel.silhouette.mixin;

import com.google.gson.JsonSyntaxException;
import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteVisualStyle;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

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

    @Inject(method = "loadEntityOutlineShader", at = @At("HEAD"), cancellable = true)
    public void silhouette_loadEntityOutlineShader(CallbackInfo ci) {
        if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.APEX) {
            loadSilhouetteShader("entity_outline_apex");
            ci.cancel();  // cancel so the regular implementation isn't called
        } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
            loadSilhouetteShader("entity_outline_custom");
            ci.cancel();  // cancel so the regular implementation isn't called
        } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.MINECRAFT){
            // continue the function like normal
        }
    }

    private Identifier customOutlineIdentifier = new Identifier("silhouette", "shaders/post/entity_outline_custom.json");
    private ManagedShaderEffect managedShader;

    private void loadSilhouetteShader(String shaderFileName) {
        if (this.entityOutlineShader != null) {
            if (this.managedShader != null) {
                managedShader.release();
                this.managedShader = null;
            }
            this.entityOutlineShader.close();
        }
        if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.APEX) {
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
        } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
            if (SilhouetteClientMod.options().customStyle.blur) {
                customOutlineIdentifier = new Identifier("silhouette", "shaders/post/entity_outline_custom.json");
            } else {
                customOutlineIdentifier = new Identifier("silhouette", "shaders/post/entity_outline_custom_noblurdir.json");
            }
            managedShader = ShaderEffectManager.getInstance().manage(customOutlineIdentifier, effect -> {
                final float luminosity = SilhouetteClientMod.options().customStyle.luminosity;
                effect.setUniformValue("Luminosity", luminosity);
            });
            this.entityOutlineShader = managedShader.getShaderEffect();
            this.entityOutlineShader.setupDimensions(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
            this.entityOutlinesFramebuffer = this.entityOutlineShader.getSecondaryTarget("final");
        }
    }


    @Inject(method = "reload()V", at = @At("RETURN"))
    public void silhouette_reload(CallbackInfo ci) {
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
                    final int alpha = 255;
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
