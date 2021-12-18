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


//    private ShaderEffect silhouetteShader;
//    private Framebuffer silhouetteFrameBuffer;

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

    // TODO FIXME remove this stuff just testing
    @Shadow
    private ClientWorld world;
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", at = @At("RETURN"))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        boolean otherClientPlayerEntityExists = StreamSupport.stream(this.world.getEntities().spliterator(), false).anyMatch(x -> x instanceof OtherClientPlayerEntity);
        if (otherClientPlayerEntityExists) {
//            SilhouetteClientMod.LOGGER.info("other client player entity exists");
        }
    }


        //    @Inject(method = "close", at = @At("RETURN"))
//    public void silhouette_close(CallbackInfo ci) {
//        closeSilhouetteShader();
//    }

//    private void closeSilhouetteShader() {
//        if (silhouetteShader != null) {
//            silhouetteShader.close();
//        }
//    }

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    // FIXME delete this i just testing stuff trying to inject my own shader
    @Inject(method = "loadEntityOutlineShader", at = @At("HEAD"), cancellable = true)
    public void silhouette_loadEntityOutlineShader(CallbackInfo ci) {

        SilhouetteClientMod.LOGGER.info("here!!! loadEntityOutlineShader");

        if (SilhouetteClientMod.options().style == SilhouetteVisualStyle.APEX) {
            SilhouetteClientMod.LOGGER.info("silhouette loading apex outline shader");
            loadSilhouetteShader();
            ci.cancel();  // cancel so the regular implementation isn't called
        } else {
            SilhouetteClientMod.LOGGER.info("silhouette loading normal outline shader");
            // continue the function like normal
        }

    }

    private void loadSilhouetteShader() {
        if (this.entityOutlineShader != null) {
            this.entityOutlineShader.close();
        }

//        Identifier identifier = null;
//        if (SilhouetteClientMod.options().style == SilhouetteVisualStyle.APEX) {
//            // get my version of entity outline shader
//            identifier = new Identifier("silhouette", "shaders/post/entity_outline.json");
//            SilhouetteClientMod.LOGGER.info("silhouette loading apex outline shader");
//        } else {
//            // normal version
//            identifier = new Identifier("shaders/post/entity_outline.json");
//            SilhouetteClientMod.LOGGER.info("silhouette loading normal outline shader");
//        }

        Identifier identifier = new Identifier("silhouette", "shaders/post/entity_outline.json");

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

//
//    private void loadSilhouetteShader() {
//        if (silhouetteShader != null) {
//            silhouetteShader.close();
//        }
//
//        Identifier identifier = new Identifier("shaders/post/entity_outline.json");
//
//        try {
//            silhouetteShader = new ShaderEffect(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), identifier);
//            silhouetteShader.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
//            silhouetteFrameBuffer = this.silhouetteShader.getSecondaryTarget("final");
//        } catch (IOException ex) {
//            SilhouetteClientMod.LOGGER.warn(String.format("failed to load shader %s: %s", identifier, ex));
//            silhouetteShader = null;
//            silhouetteFrameBuffer = null;
//        } catch (JsonSyntaxException ex) {
//            SilhouetteClientMod.LOGGER.warn(String.format("failed to parse shader %s: %s", identifier, ex));
//            silhouetteShader = null;
//            silhouetteFrameBuffer = null;
//        }
//    }

//    @Inject(method = "onResized", at = @At("RETURN"))
//    public void onResized(int width, int height, CallbackInfo ci) {
//        if (silhouetteShader != null) {
//            silhouetteShader.setupDimensions(width, height);
//        }
//    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
                              MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {

//        if (entity instanceof OtherClientPlayerEntity) {
//            double distance = entityRenderDispatcher.getSquaredDistanceToCamera(entity);
//            SilhouetteClientMod.LOGGER.info(String.format("renderEntity OtherClientPlayerEntity distance=%f", distance));
//        }

        if (shouldRenderEntityOutline(entity, vertexConsumers)) {

            if (SilhouetteClientMod.options().outlineOnlyWhenFullyHidden) {
                boolean anyPixelShowsPlayer = PixelRaycast.pixelRaycastHitsOtherPlayer(matrices, tickDelta);
                if (anyPixelShowsPlayer) {
                    // make outline "invisible"
                    OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider) vertexConsumers;

//                    ((OutlineVertexConsumerProviderMixin) outlineVertexConsumers).setShouldDraw(false);

//                    matrices.push();
//                    matrices.scale(0, 0, 0);
                    outlineVertexConsumers.setColor(HIDE_RGBA, HIDE_RGBA, HIDE_RGBA, HIDE_RGBA);
//                    matrices.pop();

                } else {
                    // normal stuff

                    OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider) vertexConsumers;

                    if (SilhouetteClientMod.options().style == SilhouetteVisualStyle.MINECRAFT) {
                        final int red = 255;
                        final int green = 255;
                        final int blue = 255;
                        final int alpha = 255;
                        outlineVertexConsumers.setColor(red, green, blue, alpha);
                    } else if (SilhouetteClientMod.options().style == SilhouetteVisualStyle.APEX) {
                        final int red = 155;
                        final int green = 215;
                        final int blue = 255;
                        int alpha = 255;
                        outlineVertexConsumers.setColor(red, green, blue, alpha);
                    }

                }
            } else {
                OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider) vertexConsumers;

                if (SilhouetteClientMod.options().style == SilhouetteVisualStyle.MINECRAFT) {
                    final int red = 255;
                    final int green = 255;
                    final int blue = 255;
                    final int alpha = 255;
                    outlineVertexConsumers.setColor(red, green, blue, alpha);
                } else if (SilhouetteClientMod.options().style == SilhouetteVisualStyle.APEX) {
                    final int red = 155;
                    final int green = 215;
                    final int blue = 255;
                    int alpha = 255;
                    outlineVertexConsumers.setColor(red, green, blue, alpha);
                }
            }




//                // TODO render within range
//                if (entity instanceof OtherClientPlayerEntity otherPlayer) {
//                    if (client.player != null) {
////                        final float distanceToOtherPlayer = client.player.distanceTo(playerEntity);
////                    client.world.
//                    }
//                }
        }

    }

    private boolean shouldRenderEntityOutline(Entity entity, VertexConsumerProvider vertexConsumers) {
        boolean isPlayerEntity = entity instanceof PlayerEntity;
        boolean isOutlineVertexConsumers = vertexConsumers instanceof OutlineVertexConsumerProvider;
        boolean isModEnabled = SilhouetteClientMod.options().isEnabled;
        boolean shouldRender = isPlayerEntity && isOutlineVertexConsumers && isModEnabled;
        return shouldRender;
    }


}
