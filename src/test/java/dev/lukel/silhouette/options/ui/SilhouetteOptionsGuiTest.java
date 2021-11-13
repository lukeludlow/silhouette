//package dev.lukel.silhouette.options.ui;
//
//import com.terraformersmc.modmenu.gui.ModsScreen;
//import dev.lukel.silhouette.SilhouetteClientMod;
//import dev.lukel.silhouette.options.SilhouetteOptions;
//import dev.lukel.silhouette.options.ui.options.OptionPage;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.RunArgs;
//import net.minecraft.client.font.TextRenderer;
//import net.minecraft.client.gui.screen.GameMenuScreen;
//import net.minecraft.client.gui.screen.Screen;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class SilhouetteOptionsGuiTest {
//
//    private class SilhouetteOptionsGuiTestDouble extends SilhouetteOptionsGui {
//
//        public SilhouetteOptionsGuiTestDouble(Screen prevScreen) {
//            super(prevScreen);
//        }
//
//        public void setTextRenderer(TextRenderer textRenderer) {
//            this.textRenderer = textRenderer;
//        }
//    }
//
//    // nvm this isn't worth it
//    @Test
//    void setPage_shouldRebuildGui() {
//        try (MockedStatic<SilhouetteClientMod> mocked = mockStatic(SilhouetteClientMod.class)) {
//            try (MockedStatic<MinecraftClient> mockedStaticMinecraftClass = mockStatic(MinecraftClient.class)) {
//
//                TextRenderer mockTextRenderer = mock(TextRenderer.class);
//                when(mockTextRenderer.getWidth(anyString())).thenReturn(100);
//
//                MinecraftClient fuck = new MinecraftClient(new RunArgs(null, null, null, null, null));
//                MinecraftClient mockMinecraftClientInstance = mock(MinecraftClient.class);
//                mockedStaticMinecraftClass.when(MinecraftClient::getInstance).thenReturn(mockMinecraftClientInstance);
//                when(mockMinecraftClientInstance.textRenderer).thenReturn(mockTextRenderer);
//
//                SilhouetteOptions defaultOptions = new SilhouetteOptions();
//                mocked.when(SilhouetteClientMod::options).thenReturn(defaultOptions);
//                Screen mockScreen = mock(Screen.class);
//                // spy on the gui. i know this is kinda bad practice but i inherited this code so whatever.
//                OptionPage mockPage = mock(OptionPage.class);
//                TextRenderer textRenderer = mock(TextRenderer.class);
//                Screen prevScreen = new ModsScreen(new GameMenuScreen(true));
//                SilhouetteOptionsGuiTestDouble gui = spy(new SilhouetteOptionsGuiTestDouble(prevScreen));
////            SilhouetteOptionsGui gui = spy(new SilhouetteOptionsGui(prevScreen));
//                gui.setTextRenderer(mockTextRenderer);
//                gui.setPage(mockPage);
////            when(gui.rebuildGUI()).thenCallRealMethod();
//                verify(gui, times(1)).rebuildGUI();
//            }
//        }
//    }
//}