package dev.lukel.silhouette.render;

import net.minecraft.client.render.OutlineVertexConsumerProvider;
import org.junit.jupiter.api.Test;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.lukel.silhouette.render.Constants.HIDE_RGBA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class SilhouetteOutlineVertexConsumerProviderTest {

    @Test
    void draw_shouldCancelCallback_whenAllParametersAreCancelCode() {
        SilhouetteOutlineVertexConsumerProvider vertexConsumer = new SilhouetteOutlineVertexConsumerProvider(mock(OutlineVertexConsumerProvider.class));
        CallbackInfo mockCallbackInfo = mock(CallbackInfo.class);
        vertexConsumer.draw(HIDE_RGBA, HIDE_RGBA, HIDE_RGBA, HIDE_RGBA, mockCallbackInfo);
        verify(mockCallbackInfo).cancel();
    }

    @Test
    void draw_shouldNotCancelCallback_whenAnyOtherRgbaParameters() {
        SilhouetteOutlineVertexConsumerProvider vertexConsumer = new SilhouetteOutlineVertexConsumerProvider(mock(OutlineVertexConsumerProvider.class));
        CallbackInfo mockCallbackInfo = mock(CallbackInfo.class);
        vertexConsumer.draw(255, 255, 255, 255, mockCallbackInfo);
        verify(mockCallbackInfo, never()).cancel();
    }
}