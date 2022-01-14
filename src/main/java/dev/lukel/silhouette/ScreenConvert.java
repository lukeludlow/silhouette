package dev.lukel.silhouette;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// thanks to https://forums.minecraftforge.net/topic/88562-116solved-3d-to-2d-conversion/
public class ScreenConvert {
    public static FinalTriple projectToPlayerView(double target_x, double target_y, double target_z, float partialTicks) {
        /* The (centered) location on the screen of the given 3d point in the world.
         * Result is <dist right of center screen, dist up from center screen, is target in front of viewing plane> */
        MinecraftClient client = MinecraftClient.getInstance();
//        double fov = client.options.fov;
//        Matrix4f projectionMatrix = client.gameRenderer.getBasicProjectionMatrix(fov);
//        projectionMatrix.
//        Vec3d camera_pos = client.cameraEntity.getClientCameraPosVec(partialTicks);
        Vec3d camera_pos = client.gameRenderer.getCamera().getPos();
//        Vec3d cameraRotationVec = client.cameraEntity.getRotationVec(partialTicks);
        Quaternion camera_rotation_conj = client.gameRenderer.getCamera().getRotation();
        camera_rotation_conj.conjugate();
//        Vec3d cameraRotationVec = client.gameRenderer.getCamera().getRotation().t
//        Quaternion camera_rotation_conj = Quaternion.fromEulerXyz((float)cameraRotationVec.x, (float)cameraRotationVec.y, (float)cameraRotationVec.z);
//        camera_rotation_conj.conjugate();

        Vec3f result3f = new Vec3f((float) (camera_pos.x - target_x),
                (float) (camera_pos.y - target_y),
                (float) (camera_pos.z - target_z));
//        Matrix3f matrix = new Matrix3f(camera_rotation_conj);
        result3f.rotate(camera_rotation_conj);
//        result3f.transform(camera_rotation_conj);

//        // ----- compensate for view bobbing (if active) -----
//        // the following code adapted from GameRenderer::applyBobbing (to invert it)
//        Minecraft mc = getMinecraft();
//        if (mc.gameSettings.viewBobbing) {
//            Entity renderViewEntity = mc.getRenderViewEntity();
//            if (renderViewEntity instanceof PlayerEntity) {
//                PlayerEntity playerentity = (PlayerEntity) renderViewEntity;
//                float distwalked_modified = playerentity.distanceWalkedModified;
//
//                float f = distwalked_modified - playerentity.prevDistanceWalkedModified;
//                float f1 = -(distwalked_modified + f * partialTicks);
//                float f2 = MathHelper.lerp(partialTicks, playerentity.prevCameraYaw, playerentity.cameraYaw);
//                Quaternion q2 = new Quaternion(Vector3f.XP, Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, true);
//                q2.conjugate();
//                result3f.transform(q2);
//
//                Quaternion q1 = new Quaternion(Vector3f.ZP, MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, true);
//                q1.conjugate();
//                result3f.transform(q1);
//
//                Vector3f bob_translation = new Vector3f((MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F), (-Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2)), 0.0f);
//                bob_translation.setY(-bob_translation.getY());  // this is weird but hey, if it works
//                result3f.add(bob_translation);
//            }
//        }

        // ----- adjust for fov -----
        Method m;
        float fov;
        GameRenderer gameRenderer = MinecraftClient.getInstance().gameRenderer;
        try {
            m = gameRenderer.getClass().getDeclaredMethod("getFov", Camera.class, float.class, boolean.class);
        } catch (NoSuchMethodException e) {
            SilhouetteClientMod.LOGGER.error(e);
            throw new Error("getFOVModifier method not present on GameRenderer class; cannot project to player screen.", e);
        }
        m.setAccessible(true);
        try {
            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            fov = ((Double) m.invoke(gameRenderer, camera, partialTicks, true)).floatValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            SilhouetteClientMod.LOGGER.error(e);
            throw new Error("getFOVModifier invocation caused error.", e);
        }

        float half_height = (float) client.getWindow().getScaledHeight() / 2;
        float scale_factor = half_height / (result3f.getZ() * (float) Math.tan(Math.toRadians(fov / 2)));
        return new FinalTriple(-result3f.getX() * scale_factor, -result3f.getY() * scale_factor, result3f.getZ() < 0);
    }
}
