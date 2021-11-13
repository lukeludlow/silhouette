package dev.lukel.silhouette;

import dev.lukel.silhouette.options.ui.storage.OptionsFileSave;
import dev.lukel.silhouette.options.SilhouetteOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;


@ExtendWith(MockitoExtension.class)
class SilhouetteClientModTest {

    @Mock
    SilhouetteOptions mockOptions;

    SilhouetteClientMod silhouetteClientMod;

    @BeforeEach
    void beforeEach() {
        silhouetteClientMod = new SilhouetteClientMod(mockOptions);
    }

    @Test
    void options_shouldThrowWhenClientModHasntBeenInitializedYet() {
        silhouetteClientMod = new SilhouetteClientMod(null);
        // this happens if some other class calls the static .options() before the client mod has been initalized
        assertThrows(IllegalStateException.class, SilhouetteClientMod::options);
    }

    @Test
    void options_shouldReturnOptionsConfig() {
//        new SilhouetteClientMod().onInitializeClient();
//        assertInstanceOf(SilhouetteOptions.class, SilhouetteClientMod.options());
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::load).thenReturn(mockOptions);
            silhouetteClientMod.onInitializeClient();
            assertInstanceOf(SilhouetteOptions.class, SilhouetteClientMod.options());
            mocked.verify(OptionsFileSave::load);
        }
    }

    @Test
    void onInitializeClient_shouldLoadConfig() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::exists).thenReturn(true);
            mocked.when(OptionsFileSave::read).thenReturn(mockOptions);
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            silhouetteClientMod.onInitializeClient();
            mocked.verify(OptionsFileSave::load);
        }
    }

    @Test
    void onInitializeClient_shouldSaveDefaultConfigIfDoesntExistYet() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::exists).thenReturn(false);
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            silhouetteClientMod.onInitializeClient();
            mocked.verify(() -> OptionsFileSave.saveChanges(any(SilhouetteOptions.class)));
        }
    }

    // mainly do this just for code coverage
    @Test
    void defaultConstructor() {
        silhouetteClientMod = new SilhouetteClientMod();
        assertInstanceOf(SilhouetteOptions.class, SilhouetteClientMod.options());
    }


}