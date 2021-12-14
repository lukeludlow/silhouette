package dev.lukel.silhouette.render;

import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteOptions;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.mob.SpiderEntity;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class SilhouetteLivingEntityRendererTest {

    @Test
    void hasLabel_shouldReturnTrue_whenEntityIsOtherClientPlayer() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.isEnabled = true;
            options.displayGamertags = true;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouetteLivingEntityRenderer livingEntityRenderer = new SilhouetteLivingEntityRenderer(mock(LivingEntityRenderer.class));
            OtherClientPlayerEntity mockOtherPlayer = mock(OtherClientPlayerEntity.class);
            assertTrue(livingEntityRenderer.hasLabel(mockOtherPlayer));
        }
    }

    @Test
    void hasLabel_shouldReturnFalse_whenEntityIsNotOtherPlayer() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.isEnabled = true;
            options.displayGamertags = true;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouetteLivingEntityRenderer livingEntityRenderer = new SilhouetteLivingEntityRenderer(mock(LivingEntityRenderer.class));
            SpiderEntity mockEntity = mock(SpiderEntity.class);
            assertFalse(livingEntityRenderer.hasLabel(mockEntity));
        }
    }


    @Test
    void hasLabel_shouldReturnFalse_whenModIsDisabled() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.isEnabled = false;
            options.displayGamertags = true;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouetteLivingEntityRenderer livingEntityRenderer = new SilhouetteLivingEntityRenderer(mock(LivingEntityRenderer.class));
            OtherClientPlayerEntity mockOtherPlayer = mock(OtherClientPlayerEntity.class);
            assertFalse(livingEntityRenderer.hasLabel(mockOtherPlayer));
        }
    }

    @Test
    void hasLabel_shouldReturnFalse_whenDisplayGamertagsIsDisabled() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.isEnabled = true;
            options.displayGamertags = false;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouetteLivingEntityRenderer livingEntityRenderer = new SilhouetteLivingEntityRenderer(mock(LivingEntityRenderer.class));
            OtherClientPlayerEntity mockOtherPlayer = mock(OtherClientPlayerEntity.class);
            assertFalse(livingEntityRenderer.hasLabel(mockOtherPlayer));
        }
    }
}