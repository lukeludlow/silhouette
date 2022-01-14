package dev.lukel.silhouette.hud;

import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteGamertagStyle;
import dev.lukel.silhouette.options.SilhouetteVisualStyle;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GamertagHudRenderer implements HudRenderCallback {

    private static final Set<Integer> playersInsideFrustum = new HashSet<>();
    private static final Map<Integer, Integer> ticksSinceLastUpdate = new HashMap<>();
    private static final int ticksLimit = 5;

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        if (!SilhouetteClientMod.options().silhouette.insaneDistance) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        ClientWorld world = client.world;
        if (world != null) {
            List<AbstractClientPlayerEntity> players = world.getPlayers();
            players.forEach(player -> {
                if (player instanceof OtherClientPlayerEntity otherClientPlayer) {
                    updatePlayerTracking(otherClientPlayer);
                    if (!playersInsideFrustum.contains(otherClientPlayer.getId()) && !isOtherPlayerTooClose(client, otherClientPlayer)) {
                        ScreenCoords screenCoords = WorldspaceScreenConverter.projectToPlayerView(otherClientPlayer.getX(), otherClientPlayer.getY(), otherClientPlayer.getZ(), tickDelta);
                        float x = screenCoords.x + ((float)client.getWindow().getScaledWidth() / 2);
                        float y = screenCoords.y + ((float)client.getWindow().getScaledHeight() / 2);
                        if (screenCoords.isTargetInFrontOfViewPlane) {
                            drawGamertag(matrices, textRenderer, otherClientPlayer, x, y);
                        }
                    }
                }
            });
        }
    }

    private void updatePlayerTracking(OtherClientPlayerEntity player) {
        int ticks = ticksSinceLastUpdate.getOrDefault(player.getId(), 0) + 1;
        if (ticks > ticksLimit) {
            playersInsideFrustum.remove(player.getId());
            return;  // stop updating the map (idk just don't want it to hit int max lol)
        }
        ticksSinceLastUpdate.put(player.getId(), ticks);
        SilhouetteClientMod.LOGGER.info("player {} has {} ticks since last update", player.getDisplayName().getString(), ticks);
    }

    private void drawGamertag(MatrixStack matrices, TextRenderer textRenderer, OtherClientPlayerEntity otherClientPlayer, float x, float y) {
        float scale = 0.5f;  // the default/recommended scale
        if (SilhouetteClientMod.options().silhouette.style == SilhouetteVisualStyle.CUSTOM) {
            scale = (float) SilhouetteClientMod.options().customStyle.insaneDistanceGamertagSize / 10;
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

    // thanks to https://github.com/Haven-King/vivid/blob/master/src/main/java/dev/monarkhes/vivid/DrawableExtensions.java
    private void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, float centerX, float y, int color, float scale) {
        matrices.push();
        matrices.scale(scale, scale, 1F);
        matrices.translate(centerX / scale, (y - textRenderer.fontHeight / 2F) / scale, 0);
        DrawableHelper.drawCenteredText(matrices, textRenderer, text, 0, -textRenderer.fontHeight / 2, color);
        matrices.pop();
    }

    // convert to 0xAARRGGBB format
    private int rgbToHex(int red, int green, int blue) {
        // e.g. red=0, green=255, blue=0 would yield 0x00ff00
        return red * 65536 + green * 256 + blue;
    }

    // this helps so that we don't accidentally render insane mode tags at screen edges
    private boolean isOtherPlayerTooClose(MinecraftClient client, OtherClientPlayerEntity otherClientPlayer) {
        ClientPlayerEntity clientPlayer = client.player;
        float viewDistance = client.gameRenderer.getViewDistance();
        if (clientPlayer != null) {
            double distance = clientPlayer.getPos().distanceTo(otherClientPlayer.getPos());
            return distance < viewDistance;
        } else {
            return false;
        }
    }

    public static void setPlayerInsideFrustum(Entity player, boolean isInsideFrustum) {
        if (player instanceof OtherClientPlayerEntity) {
            if (isInsideFrustum) {
                playersInsideFrustum.add(player.getId());
            } else {
                playersInsideFrustum.remove(player.getId());
            }
            ticksSinceLastUpdate.put(player.getId(), 0);
        }
    }

}
