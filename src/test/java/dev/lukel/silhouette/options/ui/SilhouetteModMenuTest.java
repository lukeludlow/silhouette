package dev.lukel.silhouette.options.ui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteOptions;
import net.minecraft.client.gui.screen.Screen;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class SilhouetteModMenuTest {

    @Test
    void getModConfigScreenFactory_shouldProvideSilhouetteOptionsGui() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions defaultOptions = new SilhouetteOptions();
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(defaultOptions);
            SilhouetteModMenu menu = new SilhouetteModMenu();
            ConfigScreenFactory<?> factory = menu.getModConfigScreenFactory();
            Screen mockScreen = mock(Screen.class);
            Screen actualGui = factory.create(mockScreen);
            assertInstanceOf(SilhouetteOptionsGui.class, actualGui);
        }
    }
}