package dev.lukel.silhouette;

import dev.lukel.silhouette.options.SilhouetteVisualStyle;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;

import java.awt.image.AbstractMultiResolutionImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.sqrt;

public class GamertagHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//        TextRenderer textRenderer = MinecraftClient.getInstance().inGameHud.getTextRenderer();
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world != null) {
            List<AbstractClientPlayerEntity> players = world.getPlayers();
            for (AbstractClientPlayerEntity p : players) {
                if (p instanceof OtherClientPlayerEntity otherClientPlayer) {

                    float x = (float)otherClientPlayer.getPos().getX();
                    float y = (float)otherClientPlayer.getPos().getY();
                    textRenderer.draw(matrices, new LiteralText("x display name x"), x , y, 0xffffff);

                    Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
                    double distance = camera.getPos().squaredDistanceTo(otherClientPlayer.getPos());
                    float f = otherClientPlayer.getHeight() + 1.0f + ((float)(sqrt(distance * 0.00025f)));
                    textRenderer.draw(matrices, new LiteralText("xxx"), f, y, 0xffffff);

                    FinalTriple idkifthiswillwork = ScreenConvert.projectToPlayerView(otherClientPlayer.getX(), otherClientPlayer.getY(), otherClientPlayer.getZ(), tickDelta);
                    x = idkifthiswillwork.x;
                    y = idkifthiswillwork.y;
                    x += (float)MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
                    y += (float)MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;
                    SilhouetteClientMod.LOGGER.info("projected x={},y={}", x, y);
                    if (idkifthiswillwork.isTargetInFrontOfViewPlane) {
                        textRenderer.draw(matrices, new LiteralText("idkifthiswillwork"), x, y, 0xffffff);
                    }

                }
            }
        }
    }


}
