package dev.lukel.silhouette;

import dev.lukel.silhouette.options.SilhouetteVisualStyle;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;

import java.util.List;

public class GamertagHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        if (!SilhouetteClientMod.options().silhouette.displayGamertags) {
            return;
        }
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world != null) {
            List<AbstractClientPlayerEntity> players = world.getPlayers();
            for (AbstractClientPlayerEntity p : players) {
                if (p instanceof OtherClientPlayerEntity otherClientPlayer) {
                    FinalTriple screenCoords = ScreenConvert.projectToPlayerView(otherClientPlayer.getX(), otherClientPlayer.getY(), otherClientPlayer.getZ(), tickDelta);
                    float x = screenCoords.x;
                    float y = screenCoords.y;
                    x += (float)MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
                    y += (float)MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;
                    if (screenCoords.isTargetInFrontOfViewPlane) {
                        drawGamertag(matrices, textRenderer, otherClientPlayer, x, y);
                    }
                }
            }
        }
    }

    private void drawGamertag(MatrixStack matrices, TextRenderer textRenderer, OtherClientPlayerEntity otherClientPlayer, float x, float y) {
        float scale = 0.5f;  // the default/recommended scale
        if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
            scale = (float) SilhouetteClientMod.options().customStyle.extremeDistanceGamertagSize / 10;
        }
        int color = 0xffffff;  // the default color (white)
        if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
            int red = SilhouetteClientMod.options().customStyle.red;
            int green = SilhouetteClientMod.options().customStyle.green;
            int blue = SilhouetteClientMod.options().customStyle.blue;
            color = rgbToHex(red, green, blue);
        } else if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.APEX) {
            // i know this is bad i need to fix this oh well whatever
            int red = 155;
            int green = 215;
            int blue = 255;
            color = rgbToHex(red, green, blue);
        }
        drawCenteredText(matrices, textRenderer, otherClientPlayer.getDisplayName(), x, y, color, scale);
    }

    // convert to 0xAARRGGBB format
    int rgbToHex(int red, int green, int blue) {
        // e.g. red=0, green=255, blue=0 would yield 0x00ff00
        int hexval = red * 65536 + green * 256 + blue;
        return hexval;
    }

    // thanks to https://github.com/Haven-King/vivid/blob/master/src/main/java/dev/monarkhes/vivid/DrawableExtensions.java
    public static void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, float centerX, float y, int color, float scale) {
        matrices.push();
        matrices.scale(scale, scale, 1F);
        matrices.translate(centerX / scale, (y + textRenderer.fontHeight / 2F) / scale, 0);
        DrawableHelper.drawCenteredText(matrices, textRenderer, text, 0, -textRenderer.fontHeight / 2, color);
        matrices.pop();
    }

}
