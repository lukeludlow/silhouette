package dev.lukel.silhouette.render;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SilhouettePlayerEntityRendererTest {

//    @Mock
//    AbstractClientPlayerEntity entity;
//    @Mock
//    Text text;
//    @Mock
//    MatrixStack matrices;
//    @Mock
//    VertexConsumerProvider vertexConsumers;
//    @Mock
//    EntityRenderDispatcher dispatcher;
//    @Mock
//    TextRenderer textRenderer;
//    @Mock
//    CallbackInfo ci;
//    final int light = 1;

//    @BeforeEach
//    void beforeEach() {
//        // setup default returns for mocks
//        when(dispatcher.getSquaredDistanceToCamera(entity)).thenReturn(0.0);
//    }


//    @Test
//    void renderLabelIfPresent_shouldDoSomethingAndCancelCallback_whenDisplayGamertagsEnabled() {
//        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
//            SilhouetteOptions options = new SilhouetteOptions();
//            options.isEnabled = true;
//            options.displayGamertags = true;
//            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
//            SilhouettePlayerEntityRenderer playerEntityRenderer = new SilhouettePlayerEntityRenderer();
//            playerEntityRenderer.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light, dispatcher, textRenderer, ci);
//            verify(ci).cancel();
//        }
//    }
//
//    @Test
//    void renderLabelIfPresent_shouldDoNothing_whenDisplayGamertagsDisabled() {
//        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
//            SilhouetteOptions options = new SilhouetteOptions();
//            options.isEnabled = true;
//            options.displayGamertags = false;
//            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
//            SilhouettePlayerEntityRenderer playerEntityRenderer = new SilhouettePlayerEntityRenderer();
//            playerEntityRenderer.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light, dispatcher, textRenderer, ci);
//            verify(ci, never()).cancel();
//        }
//    }
//
//    @Test
//    void renderLabelIfPresent_shouldDoNothing_whenModIsDisabled() {
//        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
//            SilhouetteOptions options = new SilhouetteOptions();
//            options.isEnabled = false;
//            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(options);
//            SilhouettePlayerEntityRenderer playerEntityRenderer = new SilhouettePlayerEntityRenderer();
//            playerEntityRenderer.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light, dispatcher, textRenderer, ci);
//            verify(ci, never()).cancel();
//        }
//    }

}