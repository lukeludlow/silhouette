package dev.lukel.silhouette.render;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.lukel.silhouette.render.Constants.MINECRAFT_DISPLAY_GAMERTAGS_DISTANCE_LIMIT;

public class SilhouettePlayerEntityRenderer {

    public void renderLabelIfPresent(AbstractClientPlayerEntity entity,
                                     Text text,
                                     MatrixStack matrices,
                                     VertexConsumerProvider vertexConsumers,
                                     int light,
                                     EntityRenderDispatcher dispatcher,
                                     TextRenderer textRenderer,
                                     CallbackInfo ci) {
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
                Matrix4f matrix4f = matrices.peek().getModel();
                float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                int j = (int) (g * 255.0F) << 24;
                float h = (float) (-textRenderer.getWidth(text) / 2);
                textRenderer.draw(text, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
                if (bl) {
                    textRenderer.draw(text, h, (float) i, -1, false, matrix4f, vertexConsumers, false, 0, light);
                }
                matrices.pop();
                ci.cancel();
            }
        }
    }
}
