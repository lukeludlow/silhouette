package dev.lukel.silhouette.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {


    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
//        ClientLoginConnectionEvents.DISCONNECT.invoker().onLoginDisconnect(this.handler, this.client);
//        ClientLoginConnectionEvents.DISCONNECT.register()
    }

    @Inject(method = "addPlayer", at = @At("HEAD"))
    public void addPlayer(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
//        SilhouetteClientMod.LOGGER.info("addPlayer here!!! id={}", id);
    }

    @Inject(method = "removeEntity(ILnet/minecraft/entity/Entity$RemovalReason;)V", at = @At("HEAD"), cancellable = true)
    public void removeEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
        Entity entity = this.getEntityLookup().get(entityId);
        if (entity instanceof OtherClientPlayerEntity) {
//            SilhouetteClientMod.LOGGER.info(String.format("removeEntity instanceof OtherClientPlayerEntity! entityId=%d reason = %s", entityId, removalReason));
//            ci.cancel();
        }
    }
//
//    @Inject(method = "disconnect", at = @At("HEAD"))
//    public void disconnect(CallbackInfo ci) {
//        SilhouetteClientMod.LOGGER.info("here! disconnect");
//    }
}
