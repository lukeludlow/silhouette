package dev.lukel.silhouette.mixin.debug;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "onEntitiesDestroy", at = @At("HEAD"), cancellable = true)
    public void onEntitiesDestroy(EntitiesDestroyS2CPacket packet, CallbackInfo ci) {
        String entityIds = packet.getEntityIds().stream().map(Object::toString).reduce(",", String::concat);
//        SilhouetteClientMod.LOGGER.info("onEntitiesDestroy here!!! entityIds={}", entityIds);
        packet.getEntityIds().forEach(entityId -> {
            Entity e = world.getEntityById(entityId);
            if (e instanceof OtherClientPlayerEntity) {
                SilhouetteClientMod.LOGGER.info("onEntitiesDestroy destroy other client player");
            }
        });
        // this very close to the actual implementation so be careful modifying to preserve behavior!
//        packet.getEntityIds().forEach(entityId -> {
//            Entity e = world.getEntityById(entityId);
//            if (e instanceof OtherClientPlayerEntity) {
//                // don't remove it
//                SilhouetteClientMod.LOGGER.info("don't destroy other client player");
//            } else {
//                world.removeEntity(entityId, Entity.RemovalReason.DISCARDED);
//            }
//        });
//        ci.cancel();
    }
}
