package dev.lukel.silhouette.options;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SilhouetteGameOptions {
    private static final String DEFAULT_FILE_NAME = "silhouette-options.json";

    public final SilhouetteSettings silhouette = new SilhouetteSettings();
    public final CustomStyle customStyle = new CustomStyle();

    private boolean readOnly;
    private Path configPath;

    public static class SilhouetteSettings {
        public boolean isEnabled = true;
        public SilhouetteVisualStyle style = SilhouetteVisualStyle.APEX;
        public SilhouetteGamertagStyle gamertagStyle = SilhouetteGamertagStyle.MINECRAFT;
        public boolean displayGamertags = true;
        public boolean insaneDistance = true;
    }

    public static class CustomStyle {
        public int red = 255;
        public int green = 255;
        public int blue = 255;
        public int luminosity = 2;
        public boolean blur = false;
        public int gamertagSize = 5;
        public int insaneDistanceGamertagSize = 5;
        public SilhouetteDummyEnum customStyleGroupTitle = SilhouetteDummyEnum.BLANK;  // dummy enum so we can have a "title" row
    }


    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    public static SilhouetteGameOptions load() {
        return load(DEFAULT_FILE_NAME);
    }

    public static SilhouetteGameOptions load(String name) {
        Path path = getConfigPath(name);
        SilhouetteGameOptions config;

        if (Files.exists(path)) {
            try (FileReader reader = new FileReader(path.toFile())) {
                config = GSON.fromJson(reader, SilhouetteGameOptions.class);
            } catch (IOException e) {
                throw new RuntimeException("Could not parse config", e);
            }
        } else {
            config = new SilhouetteGameOptions();
        }

        config.configPath = path;
        config.sanitize();

        try {
            config.writeChanges();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't update config file", e);
        }

        return config;
    }

    private void sanitize() {
//        if (this.advanced.arenaMemoryAllocator == null) {
//            this.advanced.arenaMemoryAllocator = ArenaMemoryAllocator.ASYNC;
//        }
    }

    private static Path getConfigPath(String name) {
        return FabricLoader.getInstance()
                .getConfigDir()
                .resolve(name);
    }

    public void writeChanges() throws IOException {
        if (this.isReadOnly()) {
            throw new IllegalStateException("Config file is read-only");
        }

        Path dir = this.configPath.getParent();

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        } else if (!Files.isDirectory(dir)) {
            throw new IOException("Not a directory: " + dir);
        }

        // Use a temporary location next to the config's final destination
        Path tempPath = this.configPath.resolveSibling(this.configPath.getFileName() + ".tmp");

        // Write the file to our temporary location
        Files.writeString(tempPath, GSON.toJson(this));

        // Atomically replace the old config file (if it exists) with the temporary file
        Files.move(tempPath, this.configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly() {
        this.readOnly = true;
    }

    public String getFileName() {
        return this.configPath.getFileName().toString();
    }
}