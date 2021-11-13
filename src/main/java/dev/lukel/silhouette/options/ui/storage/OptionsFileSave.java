package dev.lukel.silhouette.options.ui.storage;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteOptions;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class OptionsFileSave {

    private static final String DEFAULT_FILE_NAME = "silhouette-options.json";

    public static void saveChanges(final SilhouetteOptions options) throws IOException {

        System.out.println("here");
        final Path configPath = getConfigPath(DEFAULT_FILE_NAME);
        final Path dir = configPath.getParent();

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        } else if (!Files.isDirectory(dir)) {
            throw new IOException("Not a directory: " + dir);
        }

        // Use a temporary location next to the config's final destination
        Path tempPath = configPath.resolveSibling(configPath.getFileName() + ".tmp");

        // Write the file to our temporary location
        Files.writeString(tempPath, GSON.toJson(options));

        // Atomically replace the old config file (if it exists) with the temporary file
        Files.move(tempPath, configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    public static SilhouetteOptions load() {
        // start with default config
        SilhouetteOptions config = new SilhouetteOptions();

        if (exists()) {
            try {
                config = read();
            } catch (IOException e) {
                SilhouetteClientMod.LOGGER.error("failed to parse silhouette config file. will use default values.", e);
            }
        }

        try {
            saveChanges(config);
        } catch (IOException e) {
            SilhouetteClientMod.LOGGER.error("failed to save silhouette config file.", e);
        }

        return config;
    }

    // this should have been private but idc anymore
    public static SilhouetteOptions read() throws IOException {
        final Path configPath = getConfigPath(DEFAULT_FILE_NAME);
        final String configString = Files.readString(configPath);
        return GSON.fromJson(configString, SilhouetteOptions.class);
//        try (FileReader reader = new FileReader(configPath.toFile())) {
//            return GSON.fromJson(reader, SilhouetteOptions.class);
//        } catch (IOException e) {
//            throw e;
//        }
    }

    // this should have been private but idc anymore
    public static boolean exists() {
        return Files.exists(getConfigPath(DEFAULT_FILE_NAME));
    }

    // this should have been private but idc anymore
    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    // this should have been private but idc anymore
    public static Path getConfigPath(String name) {
        return FabricLoader.getInstance()
                .getConfigDir()
                .resolve(name);
    }
}
