package dev.lukel.silhouette;

import dev.lukel.silhouette.options.SilhouetteOptions;
import dev.lukel.silhouette.options.ui.storage.OptionsFileSave;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.RaycastContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SilhouetteClientMod implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("silhouette");
    private static SilhouetteOptions CONFIG;

    public SilhouetteClientMod() {
        this(new SilhouetteOptions());
    }

    // used for DI in unit tests
    public SilhouetteClientMod(SilhouetteOptions config) {
        CONFIG = config;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Hello Fabric world!");
        CONFIG = OptionsFileSave.load();
    }

    // gonna let this stay static even though it's annoying
    public static SilhouetteOptions options() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet available");
        }
        return CONFIG;
    }





}
