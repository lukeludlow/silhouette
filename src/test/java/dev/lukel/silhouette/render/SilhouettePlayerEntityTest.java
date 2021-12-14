package dev.lukel.silhouette.render;

import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteOptions;
import net.minecraft.entity.player.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class SilhouettePlayerEntityTest {

    @Test
    void isGlowing_shouldReturnTrue_whenModIsEnabled() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.isEnabled = true;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouettePlayerEntity playerEntity = new SilhouettePlayerEntity(mock(PlayerEntity.class));
            assertTrue(playerEntity.isGlowing());
        }
    }

    @Test
    void isGlowing_shouldReturnFalse_whenModIsDisabled() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.isEnabled = false;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouettePlayerEntity playerEntity = new SilhouettePlayerEntity(mock(PlayerEntity.class));
            assertFalse(playerEntity.isGlowing());
        }
    }

    @Test
    void shouldRenderName_shouldReturnTrue_whenDisplayGamertags() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.displayGamertags = true;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouettePlayerEntity playerEntity = new SilhouettePlayerEntity(mock(PlayerEntity.class));
            assertTrue(playerEntity.shouldRenderName());
        }
    }

    @Test
    void shouldRenderName_shouldReturnFalse_whenDontDisplayGamertags() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions options = new SilhouetteOptions();
            options.displayGamertags = false;
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
            SilhouettePlayerEntity playerEntity = new SilhouettePlayerEntity(mock(PlayerEntity.class));
            assertFalse(playerEntity.shouldRenderName());
        }
    }
}