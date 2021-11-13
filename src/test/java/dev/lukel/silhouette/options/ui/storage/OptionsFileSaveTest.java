package dev.lukel.silhouette.options.ui.storage;

import dev.lukel.silhouette.options.SilhouetteOptions;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptionsFileSaveTest {

    @Mock
    SilhouetteOptions mockOptions;

    @Test
    void load_returnsOptions() throws IOException {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            SilhouetteOptions actual = OptionsFileSave.load();
            assertInstanceOf(SilhouetteOptions.class, actual);
        }
    }

    @Test
    void load_returnsDefaultConfig_IfFileDidntExist() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::exists).thenReturn(false);
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            SilhouetteOptions expected = new SilhouetteOptions();
            SilhouetteOptions actual = OptionsFileSave.load();
            assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
        }
    }

    @Test
    void load_readsFromSavedFile_IfItExists() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            mocked.when(OptionsFileSave::exists).thenReturn(true);
            SilhouetteOptions expected = new SilhouetteOptions();
            expected.renderDistanceMax = 420;  // change whatever value in the config
            mocked.when(OptionsFileSave::read).thenReturn(expected);
            SilhouetteOptions actual = OptionsFileSave.load();
            assertEquals(expected, actual);
            assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
        }
    }

    @Test
    void load_shouldReturnDefaultConfig_WhenReadFails() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::exists).thenReturn(true);
            mocked.when(OptionsFileSave::read).thenThrow(IOException.class);
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            SilhouetteOptions expected = new SilhouetteOptions();  // default values
            SilhouetteOptions actual = OptionsFileSave.load();
            assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
        }
    }

    @Test
    void load_shouldSaveConfig() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            OptionsFileSave.load();
            mocked.verify(() -> OptionsFileSave.saveChanges(any(SilhouetteOptions.class)));
        }
    }

    // in the sodium version they return a read-only version but i don't care enough so oh well
    @Test
    void load_shouldDoNothing_IfSaveChangesFails() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            mocked.when(() -> OptionsFileSave.saveChanges(any(SilhouetteOptions.class))).thenThrow(IOException.class);
            mocked.when(OptionsFileSave::load).thenCallRealMethod();
            SilhouetteOptions expected = new SilhouetteOptions();  // default values
            SilhouetteOptions actual = OptionsFileSave.load();
            assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
        }
    }

    @Test
    void exists_shouldReturnFalse_WhenConfigFileDoesNotExist() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            Path mockPath = mock(Path.class);
            mocked.when(() -> OptionsFileSave.getConfigPath(anyString())).thenReturn(mockPath);
            mocked.when(OptionsFileSave::exists).thenCallRealMethod();
            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(false);
                assertFalse(OptionsFileSave.exists());
            }
        }
    }

    @Test
    void exists_shouldReturnTrue_WhenConfigFileExists() {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            Path mockPath = mock(Path.class);
            mocked.when(() -> OptionsFileSave.getConfigPath(anyString())).thenReturn(mockPath);
            mocked.when(OptionsFileSave::exists).thenCallRealMethod();
            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
                assertTrue(OptionsFileSave.exists());
            }
        }
    }

    @Test
    void read_fileAlreadyExists() throws IOException {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                SilhouetteOptions defaultOptions = new SilhouetteOptions();
                String defaultOptionsTextFile = OptionsFileSave.GSON.toJson(defaultOptions);
                mockedFiles.when(() -> Files.readString(any(Path.class))).thenReturn(defaultOptionsTextFile);
                mocked.when(OptionsFileSave::read).thenCallRealMethod();
                Path mockPath = mock(Path.class);
                mocked.when(() -> OptionsFileSave.getConfigPath(anyString())).thenReturn(mockPath);
                SilhouetteOptions result = OptionsFileSave.read();
                assertInstanceOf(SilhouetteOptions.class, result);
            }
        }
    }

    @Test
    void saveChanges_fileAlreadyExists_shouldUpdateAtomically() throws IOException {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                Path mockPath = mock(Path.class);
                Path mockPathParent = mock(Path.class);
                Path mockPathSibling = mock(Path.class);
                when(mockPath.getParent()).thenReturn(mockPathParent);
                when(mockPath.resolveSibling(anyString())).thenReturn(mockPathSibling);
                when(mockPath.getFileName()).thenReturn(mockPath);
                mocked.when(() -> OptionsFileSave.getConfigPath(anyString())).thenReturn(mockPath);
                mocked.when(() -> OptionsFileSave.saveChanges(any(SilhouetteOptions.class))).thenCallRealMethod();
                mockedFiles.when(() -> Files.exists(mockPathParent)).thenReturn(true);
                mockedFiles.when(() -> Files.isDirectory(mockPathParent)).thenReturn(true);
                OptionsFileSave.saveChanges(new SilhouetteOptions());
                mockedFiles.verify(() -> Files.writeString(any(Path.class), anyString()));
                mockedFiles.verify(() -> Files.move(mockPathSibling, mockPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING));
            }
        }
    }

    @Test
    void saveChanges_fileDoesntExist_shouldCreateIt() throws IOException {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                Path mockPath = mock(Path.class);
                Path mockPathParent = mock(Path.class);
                Path mockPathSibling = mock(Path.class);
                when(mockPath.getParent()).thenReturn(mockPathParent);
                when(mockPath.resolveSibling(anyString())).thenReturn(mockPathSibling);
                when(mockPath.getFileName()).thenReturn(mockPath);
                mocked.when(() -> OptionsFileSave.getConfigPath(anyString())).thenReturn(mockPath);
                mocked.when(() -> OptionsFileSave.saveChanges(any(SilhouetteOptions.class))).thenCallRealMethod();
                mockedFiles.when(() -> Files.exists(mockPathParent)).thenReturn(false);
                OptionsFileSave.saveChanges(new SilhouetteOptions());
                mockedFiles.verify(() -> Files.createDirectories(mockPathParent));
            }
        }
    }

    @Test
    void saveChanges_parentIsNotDirecotry_shouldThrow() throws IOException {
        try (MockedStatic<OptionsFileSave> mocked = mockStatic(OptionsFileSave.class)) {
            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                Path mockPath = mock(Path.class);
                Path mockPathParent = mock(Path.class);
                Path mockPathSibling = mock(Path.class);
                when(mockPath.getParent()).thenReturn(mockPathParent);
                mocked.when(() -> OptionsFileSave.getConfigPath(anyString())).thenReturn(mockPath);
                mocked.when(() -> OptionsFileSave.saveChanges(any(SilhouetteOptions.class))).thenCallRealMethod();
                mockedFiles.when(() -> Files.exists(mockPathParent)).thenReturn(true);
                mockedFiles.when(() -> Files.isDirectory(mockPathParent)).thenReturn(false);
                assertThrows(IOException.class, () -> OptionsFileSave.saveChanges(new SilhouetteOptions()));
            }
        }
    }

    @Test
    void getConfigPath_shouldResolveWithFilename() {
        try (MockedStatic<FabricLoader> mockedFabric = mockStatic(FabricLoader.class)) {
            FabricLoader mockFabricInstance = mock(FabricLoader.class);
            Path mockPath = mock(Path.class);
            mockedFabric.when(FabricLoader::getInstance).thenReturn(mockFabricInstance);
            when(mockFabricInstance.getConfigDir()).thenReturn(mockPath);
            OptionsFileSave.getConfigPath("filename");
            verify(mockPath).resolve("filename");
        }
    }
}